package com.eureka.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EurekaProvider")
public interface ConsumerFeignService {
	
	@PostMapping("provider/test1")
	public String test1(@RequestBody String message);
}
