package com.consumer01.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

@RestController
@RequestMapping("/consumer/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("getUserById")
	public User getUserById(@RequestParam(name = "id") String id) {
		return userService.getUserById(id);
	}
}