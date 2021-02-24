package com.eureka.consumer.service;

import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class ConsumerForbackFactory implements FallbackFactory<ConsumerFeignService>{

	@Override
	public ConsumerFeignService create(Throwable cause) {
		return new ConsumerFeignService() {
			
			@Override
			public String test1(String message) {
				return "熔断了"+cause.toString();
			}
		};
	}

}
