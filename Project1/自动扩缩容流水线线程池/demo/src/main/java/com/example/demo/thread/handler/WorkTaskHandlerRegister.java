package com.example.demo.thread.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkTaskHandlerRegister implements InitializingBean {

	@Autowired
	private List<TaskHandler> handlers;

	private Map<String, TaskHandler> handlerMap;

	@Override
	public void afterPropertiesSet() throws Exception {
		handlerMap = new HashMap<>();
		handlers.forEach(handler -> {
			handlerMap.put(handler.getHandlerGroup(), handler);
		});
	}

	public TaskHandler geTaskHandler(String groupCode) {
		return handlerMap.get(groupCode);
	}

}
