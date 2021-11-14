package com.study.guli.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GuliGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuliGatewayApplication.class, args);
    }

}
