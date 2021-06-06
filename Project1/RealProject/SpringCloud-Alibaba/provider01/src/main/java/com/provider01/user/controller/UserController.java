package com.provider01.user.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.api.user.domain.User;
import com.api.user.out.UserApi;

@RestController
public class UserController implements UserApi{

	@Value("${server.port}")
	private String port;
	
	private AtomicInteger num = new AtomicInteger(0);
	
	@Override
	public User getUserById(String id) {
		User user = new User();
		user.setId("port" + port + "--" + id);
		user.setName(Math.random() + "");
		int incrementAndGet = num.incrementAndGet();
		System.out.println(incrementAndGet + "--" + user.getId());
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return user;
	}

}
