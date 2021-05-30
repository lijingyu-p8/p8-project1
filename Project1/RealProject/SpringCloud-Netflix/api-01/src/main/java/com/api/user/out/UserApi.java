package com.api.user.out;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.user.domain.User;

@RequestMapping("/user")
public interface UserApi {
	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 */
	@GetMapping("getUserById")
	public User getUserById(@RequestParam String id);
}