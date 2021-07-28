package com.mq.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class Consumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("consumer1");
		defaultMQPushConsumer.setNamesrvAddr("192.168.73.40:9876");
		defaultMQPushConsumer.subscribe("myTopic", "*");
		defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
		defaultMQPushConsumer.setConsumeThreadMin(1);
		defaultMQPushConsumer.setConsumeThreadMax(5);
		defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() {
			
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				System.out.println(msgs.size());
				for (MessageExt string : msgs) {
					String str = new String(string.getBody());
					System.out.println(str);
				}
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});
		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(msgs.size());
				for (MessageExt string : msgs) {
					String str = new String(string.getBody());
					System.out.println(str);
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		defaultMQPushConsumer.start();
	}

}
