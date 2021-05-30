package com.consumer01.service.impl;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import com.consumer01.service.HealthStatusService;

@Service
public class HealthStatusServiceImpl implements HealthStatusService, HealthIndicator {

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