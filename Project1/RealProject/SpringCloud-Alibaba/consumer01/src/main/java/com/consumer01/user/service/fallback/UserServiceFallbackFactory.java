package com.consumer01.user.service.fallback;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.consumer01.user.service.UserService;

import feign.hystrix.FallbackFactory;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {

	@Value("${server.port}")
	private String port;

	@Override
	public UserService create(Throwable cause) {
		UserServiceFallBackImpl userServiceFallBackImpl = new UserServiceFallBackImpl(cause, port);
		return userServiceFallBackImpl;
	}

}
