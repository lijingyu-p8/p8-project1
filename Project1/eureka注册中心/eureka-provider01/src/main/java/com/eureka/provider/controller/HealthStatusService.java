package com.eureka.provider.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

/**
 * 手动设置服务的上下线
 * @author lijingyu
 *
 */
@Service
public class HealthStatusService implements HealthIndicator {

	private Boolean status = true;

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public Health health() {
		if (status) {
			return new Health.Builder().up().build();
		}
		return new Health.Builder().down().build();
	}

	public String getStatus() {
		return this.status.toString();
	}
}
