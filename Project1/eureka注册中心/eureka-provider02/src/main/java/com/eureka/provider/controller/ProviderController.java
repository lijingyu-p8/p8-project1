package com.eureka.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
	
	@GetMapping("getData")
	public String getData() {
		return "测试数据";
	}
	
	@PostMapping("provider/test1")
	public String test1(@RequestBody String message) {
		System.out.println("提供方02");
		return "提供方02"+message;
	}
}
