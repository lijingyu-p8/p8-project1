package com.study.rocketmq.version03;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class FilterConsumer {
	public static void main(String[] args) throws Exception {
		// 设置消费组(注意，同一组，不能关注不同的topic以及tag等的过滤，否则消息会乱)
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumerGroup01");
		defaultMQPushConsumer.setNamesrvAddr("192.168.73.200:9876");
		// 订阅topic的消息
		MessageSelector bySql = MessageSelector.bySql("age > 10");
		defaultMQPushConsumer.subscribe("topic01", bySql);
		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (int i = 0; i < msgs.size(); i++) {
					MessageExt messageExt = msgs.get(i);
					String message = new String(messageExt.getBody());
					System.out.println(message);
				}
				// 默认情况下 这条消息只会被 一个consumer 消费到 点对点
				// message 状态修改
				// ack
				// 返回消费成功
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		defaultMQPushConsumer.start();
		System.out.println("Consumer start...");
	}
}
