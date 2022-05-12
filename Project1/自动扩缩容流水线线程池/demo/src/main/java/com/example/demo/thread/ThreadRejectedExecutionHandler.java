package com.example.demo.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThreadRejectedExecutionHandler implements RejectedExecutionHandler, InitializingBean {

	@Value("${thread.pool.maxPoolSize}")
	private int maxPoolSize;

	private int maxCapacity;

	private int lastMaxPoolSize;

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		capacityExpansion(executor);
		r.run();
	}

	/**
	 * 扩容
	 * 
	 * @param executor
	 */
	private void capacityExpansion(ThreadPoolExecutor executor) {
		int activeCount = executor.getActiveCount();
		if (activeCount < maxCapacity) {
			lastMaxPoolSize = (int) (lastMaxPoolSize * 1.5);
			if (lastMaxPoolSize > maxCapacity) {
				lastMaxPoolSize = maxCapacity;
			}
			executor.setMaximumPoolSize(lastMaxPoolSize);
			System.out.println("执行了拒绝策略，扩大最大线程数成功。");
		} else {
			lastMaxPoolSize = maxPoolSize;
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		maxCapacity = maxPoolSize * 3;
		lastMaxPoolSize = maxPoolSize;
	}

}