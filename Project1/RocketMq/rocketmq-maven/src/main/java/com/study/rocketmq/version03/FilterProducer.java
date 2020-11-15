package com.study.rocketmq.version03;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class FilterProducer {
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
		//设置条件
		msg.putUserProperty("age", "16");
		// 只发送消息，不等待服务器响应，只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别
//		defaultMQProducer.sendOneway(msg);
		// 失败重投次数
		defaultMQProducer.setRetryTimesWhenSendAsyncFailed(2);
		// 异步消息发送，不能在send方法外shutdown。因为ack是异步返回的。会有时间差
		defaultMQProducer.send(msg, new SendCallback() {

			@Override
			public void onSuccess(SendResult sendResult) {
				System.out.println("发送成功");
			}

			@Override
			public void onException(Throwable e) {
				System.out.println("发送失败");
			}
		});
	}
}
