package com.example.demo.thread.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.example.demo.id.IdWorker;

/**
 * 任务分组
 * 
 * @author lijingyu
 *
 */
public class WorkTaskGroup {
	private List<WorkTask> taskList;
	private long groupId;
	private CountDownLatch countDownLatch;
	private String groupCode;

	public WorkTaskGroup(String groupCode) {
		taskList = new ArrayList<>();
//		groupId = IdWorker.buildIdWorker().nextId();
		groupId = 111L;
		countDownLatch = new CountDownLatch(1);
		this.groupCode = groupCode;
	}

	/**
	 * 添加任务
	 * 
	 * @param taskParam 任务参数
	 */
	public void addWorkTask(Object taskParam) {
		WorkTask workTask = new WorkTask(groupId, groupCode, 1);
		workTask.setParam(taskParam);
		taskList.add(workTask);
	}

	/**
	 * 添加任务 s
	 * 
	 * @param taskParam  任务参数
	 * @param retryTimes 任务失败后的重试次数
	 */
	public void addWorkTask(Object taskParam, int retryTimes) {
		WorkTask workTask = new WorkTask(groupId, groupCode, retryTimes);
		workTask.setParam(taskParam);
		taskList.add(workTask);
	}

	public void waitFinish() {
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public List<WorkTask> get() {
		return taskList;
	}

	public void finish() {
		countDownLatch.countDown();
	}

	public long getGroupId() {
		return groupId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	@Override
	public String toString() {
		return "WorkTaskGroup [taskList=" + taskList + ", groupId=" + groupId + ", countDownLatch=" + countDownLatch
				+ ", groupCode=" + groupCode + "]";
	}

}