package com.example.demo.thread.handler;

/**
 * 任务处理器
 * 
 * @author lijingyu
 *
 */
public interface TaskHandler {
	/**
	 * 处理器的标记
	 * 
	 * @return
	 */
	public String getHandlerGroup();

	/**
	 * 处理任务
	 * 
	 * @param param
	 * @return
	 */
	public Object handleTask(Object param);
}