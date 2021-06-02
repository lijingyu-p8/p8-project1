package com.consumer01.user.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.api.user.out.UserApi;
import com.consumer01.user.service.fallback.UserServiceFallback;

@FeignClient(name = "EurekaProvider",fallback = UserServiceFallback.class)
public interface UserService extends UserApi{

}