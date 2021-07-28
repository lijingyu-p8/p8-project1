package com.mq.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

public class Producer {
	public static void main(String[] args) throws Exception{
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("group1");
		defaultMQProducer.setNamesrvAddr("192.168.73.40:9876");
		defaultMQProducer.start();
//		Message msg = new Message("myTopic1", "哈哈哈dd".getBytes());
//		Message msg = new Message("myTopic", null, "1234", "测试".getBytes());
//		SendResult send = defaultMQProducer.send(msg);
		for (int i = 0; i < 20; i++) {
			Message msg = new Message("myTopic", null, "1234", ("测试" + i).getBytes());
			defaultMQProducer.send(msg, new MessageQueueSelector() {
				
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					return mqs.get(0);
				}
			}, "");
		}
		
//		System.out.println(send);
		defaultMQProducer.shutdown();
	}
}
