package com.example.demo.common;

/**
 * 异步任务异常
 * @author lijingyu
 *
 */
public class AsyncWorkThreadPoolException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2761596882212689089L;

	public AsyncWorkThreadPoolException(String message) {
		super(message);
	}

}