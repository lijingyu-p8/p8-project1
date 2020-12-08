package com.eureka.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
	
	@GetMapping("getData")
	public String getData() {
		return "测试数据";
	}
}
