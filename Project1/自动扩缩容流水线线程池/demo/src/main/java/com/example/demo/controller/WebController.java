package com.example.demo.controller;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.CommonConstants;
import com.example.demo.thread.WorkTaskProducer;
import com.example.demo.thread.runner.WorkTaskRunnerAssemblyLIne;
import com.example.demo.thread.task.WorkTaskGroup;

@RestController()
@RequestMapping("task")
public class WebController {

	@Autowired
	private WorkTaskProducer workTaskProducer;

	@Autowired
	@Qualifier("asyncThreadPool")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private WorkTaskRunnerAssemblyLIne workTaskRunnerAssemblyLIne;

	@GetMapping("addTask")
	public String addTask() {
		long start = System.currentTimeMillis();
		WorkTaskGroup workTaskGroup = new WorkTaskGroup(CommonConstants.TEST_GROUP);
		for (int j = 0; j < 1000; j++) {
			workTaskGroup.addWorkTask("ceshi:" + j);
		}
		workTaskProducer.send(workTaskGroup);
		workTaskGroup.waitFinish();
//		List<WorkTask> list = workTaskGroup.get();
//		list.stream().forEach(task -> {
//			System.out.println(task.toString());
//		});

		long end = System.currentTimeMillis();
		long time = end - start;
		return "发送成功，耗时" + time + "毫秒";
	}

	@GetMapping("monitor")
	public String monitorThreadPool() {
		ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		int activeCount = threadPoolExecutor.getActiveCount();
		StringBuilder builder = new StringBuilder();
		builder.append("核心线程数：" + threadPoolExecutor.getCorePoolSize() + " -- " + "最大线程数："
				+ threadPoolTaskExecutor.getMaxPoolSize() + " -- " + "当前活跃线程数：" + activeCount);
		int queueSize = threadPoolExecutor.getQueue().size();
		builder.append("\r\n 当前队列数量：" + queueSize);
		return builder.toString();
	}

	@GetMapping("process")
	public String getSpeedOfProcess() {
		return workTaskRunnerAssemblyLIne.speedOfProgress(111L);
	}

}