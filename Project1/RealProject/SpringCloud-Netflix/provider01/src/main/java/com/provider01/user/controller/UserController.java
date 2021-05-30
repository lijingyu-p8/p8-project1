package com.provider01.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.api.user.domain.User;
import com.api.user.out.UserApi;

@RestController
public class UserController implements UserApi{

	@Value("${server.port}")
	private String port;
	
	@Override
	public User getUserById(String id) {
		User user = new User();
		user.setId("port" + port + "--" + id);
		user.setName(Math.random() + "");
		return user;
	}

}
