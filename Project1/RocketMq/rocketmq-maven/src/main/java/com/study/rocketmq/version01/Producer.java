package com.study.rocketmq.version01;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class Producer {
	public static void main(String[] args) throws Exception {
		// 设置producer组
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("producerGroup01");
		// 设置nameserver的服务地址
		defaultMQProducer.setNamesrvAddr("192.168.73.200:9876");
		// 启动
		defaultMQProducer.start();
		String message = "rocketmq的第一条消息，发送时间：" + System.currentTimeMillis();
		// topic 消息将要发送到的地址
		// body 消息中的具体数据
		Message msg = new Message("topic01", message.getBytes());
		defaultMQProducer.send(msg);
		//关闭连接
		defaultMQProducer.shutdown();
	}
}
