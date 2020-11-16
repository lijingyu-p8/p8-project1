package com.study.rocketmq.version04;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class TransactionProducer {
	public static void main(String[] args) throws Exception {
		// 设置producer组
		TransactionMQProducer defaultMQProducer = new TransactionMQProducer("producerGroup01");
		// 设置nameserver的服务地址
		defaultMQProducer.setNamesrvAddr("192.168.73.200:9876");
		// 设置事务监听，broker会回调
		defaultMQProducer.setTransactionListener(new TransactionListener() {

			@Override
			public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
				// 未知状态
//				LocalTransactionState.UNKNOW;
				// 回滚
//				LocalTransactionState.ROLLBACK_MESSAGE;
				// 提交
				return LocalTransactionState.COMMIT_MESSAGE;
			}

			@Override
			public LocalTransactionState checkLocalTransaction(MessageExt msg) {
				return null;
			}
		});
		// 启动
		defaultMQProducer.start();
		String message = "rocketmq的第一条消息，发送时间：" + System.currentTimeMillis();
		// topic 消息将要发送到的地址
		// body 消息中的具体数据
		Message msg = new Message("topic01", message.getBytes());
		// 设置条件
		msg.putUserProperty("age", "16");
	}
}
