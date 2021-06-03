package com.cloud.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Value("${testvalue}")
	private String value;

	@GetMapping("getValue")
	public String ttt() {
		return this.value;
	}
}
