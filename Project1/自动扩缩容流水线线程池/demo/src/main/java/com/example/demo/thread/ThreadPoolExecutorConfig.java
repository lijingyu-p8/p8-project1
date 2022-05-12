package com.example.demo.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolExecutorConfig {
	
	private static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	
	@Value("${thread.pool.maxPoolSize}")
	private int maxPoolSize;
	
	@Value("${thread.pool.corePoolSize}")
	private int corePoolSize;
	
	@Value("${thread.pool.queueCapacity}")
	private int queueCapacity;
	
	@Autowired
	private ThreadRejectedExecutionHandler threadRejectedExecutionHandler;
	
	@Bean(name = "asyncThreadPool")
	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		// 核心线程数
		threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
		// 最大线程数
		threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
		// 空闲线程保持活跃时间
		threadPoolTaskExecutor.setKeepAliveSeconds(10);
		// 是否允许核心线程过期
		threadPoolTaskExecutor.setAllowCoreThreadTimeOut(false);
		// LinkedBlockingQueue队列大小
		threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
		// 线程工厂
		threadPoolTaskExecutor.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
//				thread.setName("异步线程池-" + IdWorker.buildIdWorker().nextId());
				thread.setName("异步线程池-" + atomicInteger.incrementAndGet());
				return thread;
			}
		});
		// 拒绝策略
		threadPoolTaskExecutor.setRejectedExecutionHandler(threadRejectedExecutionHandler);
		return threadPoolTaskExecutor;
	}

}