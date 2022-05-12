package com.example.demo.thread.task;

import com.example.demo.id.IdWorker;

/**
 * 单个任务
 * 
 * @author lijingyu
 *
 */
public class WorkTask {
	/**
	 * 任务状态
	 */
	private TaskStatus taskStatus;
	/**
	 * 任务参数
	 */
	private Object param;
	/**
	 * 任务ID
	 */
	private final long taskId;
	/**
	 * 分组ID
	 */
	private final long groupId;
	/**
	 * 分组Code
	 */
	private final String groupCode;
	/**
	 * 重试次数
	 */
	private int retryTimes;
	/**
	 * 重试总次数
	 */
	private int tryCount;
	/**
	 * 执行结果
	 */
	private Object result;
	
	private String info;

	public WorkTask(long groupId,String groupCode,int retryTimes) {
		taskId = IdWorker.buildIdWorker().nextId();
		this.groupId = groupId;
		this.groupCode = groupCode;
		this.retryTimes = retryTimes;
		this.taskStatus = TaskStatus.RUNNING;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public long getTaskId() {
		return taskId;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public long getGroupId() {
		return groupId;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "WorkTask [taskStatus=" + taskStatus + ", param=" + param + ", taskId=" + taskId + ", groupId=" + groupId
				+ ", groupCode=" + groupCode + ", retryTimes=" + retryTimes + ", tryCount=" + tryCount + ", result="
				+ result + ", info=" + info + "]";
	}

}