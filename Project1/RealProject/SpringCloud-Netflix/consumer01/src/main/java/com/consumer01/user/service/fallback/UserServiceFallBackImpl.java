package com.consumer01.user.service.fallback;

import com.api.user.domain.User;
import com.consumer01.user.service.UserService;

public class UserServiceFallBackImpl implements UserService{

	private Throwable cause;
	
	private String port;
	
	public UserServiceFallBackImpl(Throwable cause,String port) {
		super();
		this.cause = cause;
		this.port = port;
	}

	@Override
	public User getUserById(String id) {
		User user = new User();
		user.setId(port + "熔断了" + id);
		return user;
	}

}
