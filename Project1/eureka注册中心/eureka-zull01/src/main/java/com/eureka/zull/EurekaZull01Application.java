package com.eureka.zull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class EurekaZull01Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaZull01Application.class, args);
	}

}
