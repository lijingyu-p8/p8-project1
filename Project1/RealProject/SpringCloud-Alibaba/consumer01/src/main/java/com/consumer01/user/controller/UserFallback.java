package com.consumer01.user.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class UserFallback {
	
	public static String getConfigTest(BlockException ex) {
		return "block 了，哈哈哈" + ex;
	}
	
	public static String getConfigTest(Throwable throwable) {
		return "异常 了，哈哈哈" + throwable;
	}
}
