package com.consumer01.user.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.api.user.out.UserApi;

@FeignClient(name = "EurekaProvider")
public interface UserService extends UserApi{

}