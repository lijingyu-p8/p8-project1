package com.consumer01.user.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.api.user.out.UserApi;
import com.consumer01.user.service.fallback.UserServiceFallback;
import com.consumer01.user.service.fallback.UserServiceFallbackFactory;

//@FeignClient(name = "EurekaProvider",fallback = UserServiceFallback.class)
@FeignClient(name = "EurekaProvider",fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService extends UserApi{

}