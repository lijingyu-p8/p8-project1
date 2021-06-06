package com.consumer01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.consumer01.service.HealthStatusService;

@RestController
public class HealthStatusController {

	@Autowired
	private HealthStatusService healthStatusService;

	@GetMapping("/health")
	public String health(@RequestParam("status") Boolean status) {
		healthStatusService.setStatus(status);
		return healthStatusService.getStatus();
	}
}
