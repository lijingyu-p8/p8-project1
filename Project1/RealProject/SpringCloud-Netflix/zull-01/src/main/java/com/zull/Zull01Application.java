package com.zull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class Zull01Application {

	public static void main(String[] args) {
		SpringApplication.run(Zull01Application.class, args);
	}

}
