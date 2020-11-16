package com.study.rocketmq.version05;

import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByRandom;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

public class QueueProducer {
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
		// 设置条件
		msg.putUserProperty("age", "16");
		// 内置的几个实现
		SelectMessageQueueByHash selectMessageQueueByHash = new SelectMessageQueueByHash();
		SelectMessageQueueByRandom selectMessageQueueByRandom = new SelectMessageQueueByRandom();
		defaultMQProducer.send(msg, new MessageQueueSelector() {

			/**
			 * mqs：指定topic下的所有队列，默认是4个 msg：发送的消息 arg：send方法传递的args
			 */
			@Override
			public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
				// 可以通过一系列算法计算返回的队列
				return mqs.get(0);
			}
		}, 1);
	}
}
