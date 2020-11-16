package com.study.rocketmq.version05;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

public class QueueConsumer {
	public static void main(String[] args) throws Exception {
		// 设置消费组(注意，同一组，不能关注不同的topic以及tag等的过滤，否则消息会乱)
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumerGroup01");
		defaultMQPushConsumer.setNamesrvAddr("192.168.73.200:9876");
		// 订阅topic的消息
		MessageSelector bySql = MessageSelector.bySql("age > 10");
		defaultMQPushConsumer.subscribe("topic01", bySql);
		// 这个监听器会并发的，开启多个线程进行消费
//		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
//
//			@Override
//			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//				for (int i = 0; i < msgs.size(); i++) {
//					MessageExt messageExt = msgs.get(i);
//					String message = new String(messageExt.getBody());
//					System.out.println(message);
//				}
//				// 默认情况下 这条消息只会被 一个consumer 消费到 点对点
//				// message 状态修改
//				// ack
//				// 返回消费成功
//				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//			}
//		});
		// 最大线程数
		defaultMQPushConsumer.setConsumeThreadMax(10);
		// 最小线程数
		defaultMQPushConsumer.setConsumeThreadMin(2);
		// 顺序消费 一个queue一个线程。多个queue多个线程 
		// 同一个queue同一个线程是顺序消费的
		defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() {

			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				return null;
			}
		});
		defaultMQPushConsumer.start();
		System.out.println("Consumer start...");
	}
}
