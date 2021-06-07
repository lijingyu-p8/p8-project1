package com.consumer01.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

@RestController
@RequestMapping("/consumer/user")
@RefreshScope
public class UserController {

	@Autowired
	private UserService userService;

	@Value("${my.name}")
	private String name;

	@Value("${server.port}")
	private String port;

	@GetMapping("getUserById")
	public User getUserById(@RequestParam(name = "id") String id) {
		return userService.getUserById(id);
	}

	@SentinelResource(value = "/consumer/user/getconfig", blockHandler = "getConfigTest", blockHandlerClass = {
			UserFallback.class }, fallback = "getConfigTest", fallbackClass = { UserFallback.class })
	@GetMapping("getConfig")
	public String getConfigTest() {
//		int i = 0;
//		System.out.println(10/i);
		return "端口-" + port + ";my.name--" + name;
	}

}