package com.study.rocketmq.version01;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class SyncProducer {
	public static void main(String[] args) throws Exception {
		// 设置producer组
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("L_producerGroup02");
		// 设置nameserver的服务地址
		defaultMQProducer.setNamesrvAddr("10.2.20.39:9877");
		// 启动
		defaultMQProducer.start();
		String message = "rocketmq的第一条消息，发送时间：" + System.currentTimeMillis();
		// topic 消息将要发送到的地址
		// body 消息中的具体数据
		Message msg = new Message("L_topic02", message.getBytes());
		// 同步消息发送，等到broker的ack之后才会结束
		defaultMQProducer.send(msg);

		// 可以批量发送
		// 批量消息要求必要具有同一topic、相同消息配置
		// 不支持延时消息
		// 建议一个批量消息最好不要超过1MB大小
		// 如果不确定是否超过限制，可以手动计算大小分批发送
		List<Message> list = new ArrayList<>();
		list.add(msg);
		defaultMQProducer.send(list);
		// 关闭连接
		defaultMQProducer.shutdown();
	}
}
