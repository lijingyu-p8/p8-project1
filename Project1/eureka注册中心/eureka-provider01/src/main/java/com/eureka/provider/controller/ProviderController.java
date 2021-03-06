package com.eureka.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
	@Autowired
	HealthStatusService healthStatusService;
	
	@GetMapping("getData")
	public String getData() {
		return "测试数据";
	}
	
	@GetMapping("health")
	public String health(@RequestParam("status") Boolean status) {
		healthStatusService.setStatus(status);
		return status.toString();
	}
	
	@PostMapping("provider/test1")
	public String test1(@RequestBody String message) {
		System.out.println("提供方01");
		return "提供方01"+message;
	}
}
