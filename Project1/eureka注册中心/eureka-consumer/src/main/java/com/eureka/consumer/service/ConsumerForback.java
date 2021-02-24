package com.eureka.consumer.service;

import org.springframework.stereotype.Component;

@Component
public class ConsumerForback implements ConsumerFeignService{

	@Override
	public String test1(String message) {
		return "熔断了";
	}

}
