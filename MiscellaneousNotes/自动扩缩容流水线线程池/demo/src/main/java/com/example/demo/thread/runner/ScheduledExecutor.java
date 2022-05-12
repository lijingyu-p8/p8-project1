package com.example.demo.thread.runner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.example.demo.id.IdWorker;

@Component
public class ScheduledExecutor implements InitializingBean {

	@Autowired
	private WorkTaskRunnerAssemblyLIne workTaskRunnerAssemblyLIne;

	@Autowired
	@Qualifier("asyncThreadPool")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Value("${thread.pool.maxPoolSize}")
	private int maxPoolSize;

	@Value("${thread.pool.corePoolSize}")
	private int corePoolSize;

	@Value("${thread.pool.queueCapacity}")
	private int queueCapacity;

	@Override
	public void afterPropertiesSet() throws Exception {
		runTask();
		runDeathTask();
		runRemoveSuccess();
		monitorThreadPool();
	}

	private void runTask() {
		// 定时读取任务队列
		ScheduledExecutorService singleThreadScheduledExecutor = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setName("【定时任务线程】消费任务队列-" + IdWorker.buildIdWorker().nextId());
						return thread;
					}
				});
		singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// 消费队列中的任务
				workTaskRunnerAssemblyLIne.consumeTask();
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	private void runDeathTask() {
		ScheduledExecutorService singleThreadScheduledExecutor = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setName("【定时任务线程】处理失败任务队列-" + IdWorker.buildIdWorker().nextId());
						return thread;
					}
				});
		singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				workTaskRunnerAssemblyLIne.consumeDeathTask();
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	private void runRemoveSuccess() {
		ScheduledExecutorService singleThreadScheduledExecutor = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setName("【定时任务线程】移除成功任务-" + IdWorker.buildIdWorker().nextId());
						return thread;
					}
				});
		singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				workTaskRunnerAssemblyLIne.removeSuccess();
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	private void monitorThreadPool() {
		// 定时读取任务队列
		ScheduledExecutorService singleThreadScheduledExecutor = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setName("【定时任务线程】监控线程池-" + IdWorker.buildIdWorker().nextId());
						return thread;
					}
				});
		singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
				int queueSize = threadPoolExecutor.getQueue().size();
				int activeCount = threadPoolExecutor.getActiveCount();
				int upperLimit = (int) (queueCapacity * 0.75);
				if (queueSize > 0 && queueSize >= upperLimit && activeCount <= maxPoolSize) {
					threadPoolExecutor.setCorePoolSize(maxPoolSize);
					System.out.println("线程数量成功提前增加到最大线程数");
				} else if (queueSize == 0 && activeCount > corePoolSize) {
					threadPoolExecutor.setCorePoolSize(corePoolSize);
					System.out.println("线程数量成功还原初始值");
				} else if (queueSize == 0 && threadPoolExecutor.getMaximumPoolSize() > maxPoolSize) {
					threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
					System.out.println("线程数量成功还原初始值");
				}
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
}