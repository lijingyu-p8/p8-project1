package com.example.demo.thread.runner;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.demo.common.AsyncWorkThreadPoolException;
import com.example.demo.common.CommonAssert;
import com.example.demo.common.CommonConstants;
import com.example.demo.thread.handler.TaskHandler;
import com.example.demo.thread.handler.WorkTaskHandlerRegister;
import com.example.demo.thread.task.TaskStatus;
import com.example.demo.thread.task.WorkTask;
import com.example.demo.thread.task.WorkTaskGroup;

@Component
public class WorkTaskRunnerAssemblyLIne {

	/**
	 * 提交任务的队列
	 */
	private LinkedBlockingQueue<WorkTask> taskBlockingQueue = new LinkedBlockingQueue<>(200);
	/**
	 * 任务分组id
	 */
	private ConcurrentHashMap<Long, WorkTaskGroup> taskGroupIdConcurrentHashMap = new ConcurrentHashMap<>();
	/**
	 * 任务分组执行完的任务数
	 */
	private ConcurrentHashMap<Long, AtomicInteger> taskGroupComplateTaskMap = new ConcurrentHashMap<>();
	/**
	 * 执行失败的任务队列
	 */
	private LinkedBlockingQueue<WorkTask> taskDeathQueue = new LinkedBlockingQueue<>(200);
	/**
	 * 执行成功的分组
	 */
	private LinkedBlockingQueue<WorkTaskGroup> taskSuccessGroupQueue = new LinkedBlockingQueue<>(200);

	@Autowired
	@Qualifier("asyncThreadPool")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private WorkTaskHandlerRegister workTaskHandlerRegister;

	public void addTaskGroup(WorkTaskGroup workTaskGroup) {
		CommonAssert.notNull(workTaskGroup, "任务分组不能为空");
		if (workTaskGroup.get().isEmpty()) {
			throw new AsyncWorkThreadPoolException("提交的任务不能为空");
		}
		if (taskGroupIdConcurrentHashMap.contains(workTaskGroup.getGroupId())) {
			throw new AsyncWorkThreadPoolException("当前任务分组已在执行队列中，不能重复提交");
		}
		// 添加分组缓存
		taskGroupIdConcurrentHashMap.put(workTaskGroup.getGroupId(), workTaskGroup);
		// 添加分组任务执行完成数量
		taskGroupComplateTaskMap.put(workTaskGroup.getGroupId(), new AtomicInteger(0));
		// 队列中添加任务
		List<WorkTask> list = workTaskGroup.get();
		for (int i = 0; i < list.size(); i++) {
			try {
				taskBlockingQueue.put(list.get(i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 进度
	 * 
	 * @return
	 */
	public String speedOfProgress(Long groupId) {
		StringBuilder builder = new StringBuilder();
		if (!taskGroupIdConcurrentHashMap.containsKey(groupId)) {
			return "任务已经执行完毕。";
		}
		WorkTaskGroup workTaskGroup = taskGroupIdConcurrentHashMap.get(groupId);
		int size = workTaskGroup.get().size();
		int complateSize = taskGroupComplateTaskMap.get(groupId).get();
		builder.append("任务分组id：" + workTaskGroup.getGroupId());
		builder.append(" -- ");
		builder.append("任务分组code：" + workTaskGroup.getGroupCode());
		builder.append(" -- ");
		builder.append("任务总数：" + size);
		builder.append(" -- ");
		builder.append("已完成任务总数：" + complateSize);
		builder.append(" -- ");
		String speed = ((float) complateSize / (float) size) * 100 + "%";
		builder.append("执行进度：" + speed);
		return builder.toString();
	}

	/**
	 * 取消
	 * 
	 * @param groupId
	 */
	public void cancel(Long groupId) {
		if (!taskGroupIdConcurrentHashMap.contains(groupId)) {
			return;
		}
		WorkTaskGroup workTaskGroup = taskGroupIdConcurrentHashMap.get(groupId);
		workTaskGroup.finish();
		taskGroupIdConcurrentHashMap.remove(groupId);
		taskGroupComplateTaskMap.remove(groupId);
	}

	/**
	 * 消费处理任务
	 */
	public void consumeTask() {
		if (taskBlockingQueue.isEmpty()) {
			return;
		}
		int taskSize = taskBlockingQueue.size();
		for (int i = 0; i < taskSize; i++) {
			WorkTask workTask = taskBlockingQueue.poll();
			TaskHandler taskHandler = workTaskHandlerRegister.geTaskHandler(workTask.getGroupCode());
			if (taskHandler == null || !taskGroupIdConcurrentHashMap.containsKey(workTask.getGroupId())) {
				workTask.setTaskStatus(TaskStatus.FAIL);
				processTask(workTask);
				continue;
			}
			int tryCount = workTask.getTryCount();
			int retryTimes = workTask.getRetryTimes();
			// 超过重试次数，任然执行失败
			if (tryCount > retryTimes) {
				try {
					taskDeathQueue.put(workTask);
					workTask.setTaskStatus(TaskStatus.FAIL);
					workTask.setInfo(CommonConstants.TASK_RETRY_DEATH_INFO);
					processTask(workTask);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			ListenableFuture<Object> listenableFuture = threadPoolTaskExecutor.submitListenable(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					if (!taskGroupIdConcurrentHashMap.containsKey(workTask.getGroupId())) {
						return CommonConstants.HAVING_CONCEL;
					}
					Object result = taskHandler.handleTask(workTask.getParam());
					return result;
				}
			});
			listenableFuture.addCallback(new ListenableFutureCallback<Object>() {

				@Override
				public void onSuccess(Object result) {
					// 执行成功
					String name = Thread.currentThread().getName();
//					System.out.println("执行成功的回调 -- "+name + "-执行成功");
					if (result != null && CommonConstants.HAVING_CONCEL.equals(result)) {
						workTask.setTaskStatus(TaskStatus.FAIL);
						workTask.setInfo(CommonConstants.HAVING_CONCEL_INFO);
					} else {
						workTask.setTaskStatus(TaskStatus.SUCCESS);
					}
					workTask.setResult(result);
					processTask(workTask);
				}

				@Override
				public void onFailure(Throwable ex) {
					// 执行失败
					System.out.println("执行失败");
					try {
						workTask.setTryCount(workTask.getTryCount() + 1);
						// 执行失败，放到队列中等待重试
						taskBlockingQueue.put(workTask);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void processTask(WorkTask workTask) {
		int complateSize = taskGroupComplateTaskMap.get(workTask.getGroupId()).incrementAndGet();
		WorkTaskGroup workTaskGroup = taskGroupIdConcurrentHashMap.get(workTask.getGroupId());
		int size = workTaskGroup.get().size();
		System.out.println("执行完毕：" + complateSize + "  总共：" + size);
		// 如果执行完毕的任务数量=总任务数量
		if (complateSize == size) {
			workTaskGroup.finish();
			try {
				taskSuccessGroupQueue.put(workTaskGroup);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理失败的任务
	 */
	public void consumeDeathTask() {
		if (taskDeathQueue.isEmpty()) {
			return;
		}
		int size = taskDeathQueue.size();
//		for (int i = 0; i < size; i++) {
//			WorkTask workTask = taskDeathQueue.poll();
//			System.out.println("入库" + workTask.toString());
//		}
	}

	/**
	 * 移除成功的任务
	 */
	public void removeSuccess() {
		if (taskSuccessGroupQueue.isEmpty()) {
			return;
		}
		// 处理成功队列
		int size = taskSuccessGroupQueue.size();
		for (int i = 0; i < size; i++) {
			WorkTaskGroup poll = taskSuccessGroupQueue.poll();
			String name = Thread.currentThread().getName();
			System.out.println("移除成功的分组 -- " + name + " -- " + poll.getGroupCode());
			taskGroupIdConcurrentHashMap.remove(poll.getGroupId());
			taskGroupComplateTaskMap.remove(poll.getGroupId());
		}
	}

}