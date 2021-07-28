package com.zull.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

@Component
public class MyZullFilter extends ZuulFilter {

	@Autowired
	UrlPathHelper urlPathHelper;
	@Autowired
	RouteLocator routeLocator;
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest servletRequest = requestContext.getRequest();
		String requestURI = urlPathHelper.getPathWithinApplication(servletRequest);
        Route route = routeLocator.getMatchingRoute(requestURI);
        String serverId = route.getId();
        //serverid 查询库，是否开启了灰度发布
		String version = servletRequest.getHeader("version");
		// 第一种：查数据库，根据服务名，开启灰度，查询对应的version、用户
		// 有记录，则将version值设置为version，无记录，则不处理
		// 第二种：10%的用户，用户id获取hash值，%10 == 1 除10 模为1的，如果开启了灰度，则走灰度的
		if (isGray(version)) {
			RibbonFilterContextHolder.getCurrentContext().add("gray", "true");
		} else {
			RibbonFilterContextHolder.getCurrentContext().add("gray", "false");
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	public boolean isGray(String version) {
		if ("v2".equals(version)) {
			return true;
		}
		return false;
	}

}
