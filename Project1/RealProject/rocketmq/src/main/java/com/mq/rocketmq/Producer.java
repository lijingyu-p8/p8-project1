package com.mq.rocketmq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.ThreadFactoryImpl;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class Producer {
	public static void main(String[] args) {
		TransactionMQProducer transactionMQProducer = new TransactionMQProducer("transactionGroup1");
		transactionMQProducer.setNamesrvAddr("192.168.73.40:9876;192.168.73.41:9876;192.168.73.42:9876;"
				+ "192.168.73.50:9876;192.168.73.51:9876;192.168.73.52:9876");
		ExecutorService executorService = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(1000), new ThreadFactoryImpl("myMq"));
		transactionMQProducer.setExecutorService(executorService);
		// 设置事务监听器
		transactionMQProducer.setTransactionListener(new TransactionListener() {

			// 执行本地事务
			@Override
			public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				return LocalTransactionState.UNKNOW;
			}

			// 消息状态回查
			@Override
			public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				String name = Thread.currentThread().getName();
				System.out.println(name);
				return LocalTransactionState.COMMIT_MESSAGE;
			}
		});
		try {
			transactionMQProducer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 300; i++) {
			Message msg = new Message("myTopic", String.valueOf(i), String.valueOf(i), ("测试" + i).getBytes());
			try {
				transactionMQProducer.sendMessageInTransaction(msg, i);
			} catch (MQClientException e) {
				e.printStackTrace();
			}
		}
	}
}