package com.mq.rocketmq.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	
	public SendResult send() {
		SendResult syncSend = rocketMQTemplate.syncSend("topic", "测试消息");
		return syncSend;
	}
}
