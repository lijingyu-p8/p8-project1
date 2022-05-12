package com.example.demo.thread.handler;

import org.springframework.stereotype.Component;

import com.example.demo.common.CommonConstants;

@Component
public class MyTaskHandler implements TaskHandler {

	@Override
	public String getHandlerGroup() {
		return CommonConstants.TEST_GROUP;
	}

	@Override
	public Object handleTask(Object param) {
		String name = Thread.currentThread().getName();
		System.out.println("MyTaskHandler -- " + name + "--"+param);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}