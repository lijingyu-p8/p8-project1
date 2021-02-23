package com.eureka.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.consumer.service.ConsumerFeignService;

@RestController
public class ConsumerFeignController {
	@Autowired
	ConsumerFeignService consumerFeignService;
	
	@GetMapping("consumer/test1")
	public String test1(@RequestParam String message) {
		return consumerFeignService.test1(message);
	}
}
