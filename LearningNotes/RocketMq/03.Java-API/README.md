# RocketMQ-Java-API

## 添加依赖

```xml
<dependencies>
   <dependency>
      <groupId>org.apache.rocketmq</groupId>
      <artifactId>rocketmq-client</artifactId>
      <version>4.8.0</version>
   </dependency>
</dependencies>
```

## 消息的发送

### 1、普通消息

- 同步发送

  ```java
  public class Test {
  	public static void main(String[] args) throws Exception {
  		// 创建一个producer，参数为Producer Group名称
  		DefaultMQProducer producer = new DefaultMQProducer("pg");
  		// 指定nameServer地址
  		producer.setNamesrvAddr("rocketmqOS:9876");
  		// 设置当发送失败时重试发送的次数，默认为2次
  		producer.setRetryTimesWhenSendFailed(3);
  		// 设置发送超时时限为5s，默认3s
  		producer.setSendMsgTimeout(5000);
  		// 开启生产者
  		producer.start();
  		// 生产并发送100条消息
  		for (int i = 0; i < 100; i++) {
  			byte[] body = ("Hi," + i).getBytes();
  			Message msg = new Message("someTopic", "someTag", body);
  			// 为消息指定key
  			msg.setKeys("key-" + i);
  			// 发送消息
  			SendResult sendResult = producer.send(msg);
  			System.out.println(sendResult);
  		}
  		// 关闭producer
  		producer.shutdown();
  
  	}
  }
  ```

- 异步发送

  ```java
  public class Test {
  	public class AsyncProducer {
  		public static void main(String[] args) throws Exception {
  			DefaultMQProducer producer = new DefaultMQProducer("pg");
  			producer.setNamesrvAddr("rocketmqOS:9876");
  			// 指定异步发送失败后不进行重试发送
  			producer.setRetryTimesWhenSendAsyncFailed(0);
  			// 指定新创建的Topic的Queue数量为2，默认为4
  			producer.setDefaultTopicQueueNums(2);
  			producer.start();
  			for (int i = 0; i < 100; i++) {
  				byte[] body = ("Hi," + i).getBytes();
  				try {
  					Message msg = new Message("myTopicA", "myTag", body);
  					// 异步发送。指定回调
  					producer.send(msg, new SendCallback() {
  						// 当producer接收到MQ发送来的ACK后就会触发该回调方法的执行
  						@Override
  						public void onSuccess(SendResult sendResult) {
  							System.out.println(sendResult);
  						}
  
  						@Override
  						public void onException(Throwable e) {
  							e.printStackTrace();
  						}
  					});
  				} catch (Exception e) {
  					e.printStackTrace();
  				}
  			}
  			// end-for
  			// sleep一会儿
  			// 由于采用的是异步发送，所以若这里不sleep，
  			// 则消息还未发送就会将producer给关闭，报错
  			TimeUnit.SECONDS.sleep(300);
  			producer.shutdown();
  		}
  	}
  }
  ```

### 2、顺序消息

### 3、延时消息

### 4、事务消息

### 5、批量消息

## 消息的消费

- 普通消费

  ```java
  public class Test {
  	public static void main(String[] args) throws MQClientException {
  		// 定义一个pull消费者
  		DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cg");
  		// 定义一个push消费者
  		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg");
  		// 指定nameServer
  		consumer.setNamesrvAddr("rocketmqOS:9876");
  		// 指定(消费offset)，设置从第一条消息开始消费
  		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
  		// 指定消费topic与tag
  		consumer.subscribe("someTopic", "*");
  		// 指定采用“广播模式”进行消费，默认为“集群模式”
  		// consumer.setMessageModel(MessageModel.BROADCASTING);
  		// 注册消息监听器
  		consumer.registerMessageListener(new MessageListenerConcurrently() {
  			// 一旦broker中有了其订阅的消息就会触发该方法的执行，
  			// 其返回值为当前consumer消费的状态
  			@Override
  			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
  				// 逐条消费消息
  				for (MessageExt msg : msgs) {
  					System.out.println(msg);
  				}
  				// 返回消费状态：消费成功
  				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
  			}
  		});
  		// 开启消费者消费
  		consumer.start();
  		System.out.println("Consumer Started");
  	}
  }
  ```

  

