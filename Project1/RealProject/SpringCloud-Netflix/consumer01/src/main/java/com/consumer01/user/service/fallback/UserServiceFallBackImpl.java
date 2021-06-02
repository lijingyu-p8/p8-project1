package com.consumer01.user.service.fallback;

import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

public class UserServiceFallBackImpl implements UserService{

	private Throwable cause;
	
	public UserServiceFallBackImpl(Throwable cause) {
		super();
		this.cause = cause;
	}

	@Override
	public User getUserById(String id) {
		User user = new User();
		user.setId(cause + "熔断了" + id);
		return user;
	}

}
