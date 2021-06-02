package com.consumer01.user.service.fallback;

import org.springframework.stereotype.Component;

import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

@Component
public class UserServiceFallback implements UserService {

	@Override
	public User getUserById(String id) {
		User user = new User();
		user.setId("熔断了" + id);
		return user;
	}

}
