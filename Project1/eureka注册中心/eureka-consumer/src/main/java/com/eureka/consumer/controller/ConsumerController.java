package com.eureka.consumer.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;

@RestController
public class ConsumerController {
	@Autowired
	DiscoveryClient discoveryClient;
	@Autowired
	EurekaClient eurekaClient;
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	RestTemplate restTemplate;
	@GetMapping("loadData01")
	public String loadData() {
		List<String> services = discoveryClient.getServices();
		for (int i = 0; i < services.size(); i++) {
			System.out.println(services.get(i));
		}
		List<ServiceInstance> instances = discoveryClient.getInstances("provider");
		ServiceInstance serviceInstance = instances.get(0);
		URI uri = serviceInstance.getUri();
		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.getForObject(uri + "/getData", String.class);
		return string;
	}

	@GetMapping("loadData02")
	public String loadData02() {
		List<String> services = discoveryClient.getServices();
		for (int i = 0; i < services.size(); i++) {
			System.out.println(services.get(i));
		}
		List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress("provider", false);
		InstanceInfo instanceInfo = instances.get(0);
		String string = "";
		if (instanceInfo.getStatus() == InstanceStatus.UP) {
			String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + "/getData";
			System.out.println("url:" + url);
			RestTemplate restTemplate = new RestTemplate();
			string = restTemplate.getForObject(url, String.class);
		}
		return string;
	}

	@GetMapping("loadData03")
	public String loadData03() {
		// ribbon负载均衡
		ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
		URI uri = serviceInstance.getUri();
		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.getForObject(uri + "/getData", String.class);
		return string;
	}
	
	@GetMapping("loadData04")
	public String loadData04() {
		String url ="http://provider/getData";
		String respStr = restTemplate.getForObject(url, String.class);
		return respStr;
	}
}
