package com.mq.rocketmq.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "springboot-mq", consumerGroup = "springboot-mq-consumer-1")
public class ConsumerService implements RocketMQListener<String> {

	@Override
	public void onMessage(String message) {

	}

}
