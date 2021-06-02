package com.consumer01.user.service.fallback;

import org.springframework.stereotype.Component;

import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

import feign.hystrix.FallbackFactory;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService>{

	@Override
	public UserService create(Throwable cause) {
		UserServiceFallBackImpl userServiceFallBackImpl = new UserServiceFallBackImpl(cause);
		return userServiceFallBackImpl;
	}

}
