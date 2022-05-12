package com.example.demo.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.common.CommonAssert;
import com.example.demo.thread.runner.WorkTaskRunnerAssemblyLIne;
import com.example.demo.thread.task.WorkTaskGroup;

@Component
public class WorkTaskProducer {
	@Autowired
	private WorkTaskRunnerAssemblyLIne workTaskRunnerAssemblyLIne;
	
	public void send(WorkTaskGroup workTaskGroup) {
		CommonAssert.notNull(workTaskGroup, "任务分组不能为null");
		workTaskRunnerAssemblyLIne.addTaskGroup(workTaskGroup);
	}
}
