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
		// ����producer��
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("producerGroup01");
		// ����nameserver�ķ����ַ
		defaultMQProducer.setNamesrvAddr("192.168.73.200:9876");
		// ����
		defaultMQProducer.start();
		String message = "rocketmq�ĵ�һ����Ϣ������ʱ�䣺" + System.currentTimeMillis();
		// topic ��Ϣ��Ҫ���͵��ĵ�ַ
		// body ��Ϣ�еľ�������
		Message msg = new Message("topic01", message.getBytes());
		// ��������
		msg.putUserProperty("age", "16");
		// ���õļ���ʵ��
		SelectMessageQueueByHash selectMessageQueueByHash = new SelectMessageQueueByHash();
		SelectMessageQueueByRandom selectMessageQueueByRandom = new SelectMessageQueueByRandom();
		defaultMQProducer.send(msg, new MessageQueueSelector() {

			/**
			 * mqs��ָ��topic�µ����ж��У�Ĭ����4�� msg�����͵���Ϣ arg��send�������ݵ�args
			 */
			@Override
			public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
				// ����ͨ��һϵ���㷨���㷵�صĶ���
				return mqs.get(0);
			}
		}, 1);
	}
}
