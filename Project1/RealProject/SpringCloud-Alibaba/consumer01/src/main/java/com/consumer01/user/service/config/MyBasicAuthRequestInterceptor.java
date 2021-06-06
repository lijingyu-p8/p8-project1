package com.consumer01.user.service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 自定义配置拦截器
 * @author lijingyu
 *
 */
public class MyBasicAuthRequestInterceptor implements RequestInterceptor{

	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", "Basic cm9vdDpyb290");		
	}

}
