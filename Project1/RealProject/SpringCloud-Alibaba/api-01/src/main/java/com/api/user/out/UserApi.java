package com.api.user.out;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.user.domain.User;

public interface UserApi {
	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 */
	@GetMapping("/user/getUserById")
	public User getUserById(@RequestParam String id);
}