# RocketMQ-Java-API

## 添加依赖

- ```xml
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

### 2、顺序消息--分区有序

- 要保证消息有序，只能保证同一个queue队列内部消息有序，并且消费者应该用单线程进行指定queue内消息的消费。

  ```java
  public class Test {
  	public static void main(String[] args) throws Exception {
  		DefaultMQProducer producer = new DefaultMQProducer("pg");
  		producer.setNamesrvAddr("rocketmqOS:9876");
  		producer.start();
  		for (int i = 0; i < 100; i++) {
  			Integer orderId = i;
  			byte[] body = ("Hi," + i).getBytes();
  			Message msg = new Message("TopicA", "TagA", body);
  			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
  				@Override
  				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
  					Integer id = (Integer) arg;
  					int index = id % mqs.size();
  					return mqs.get(index);
  				}
  			}, orderId);
  			System.out.println(sendResult);
  		}
  		producer.shutdown();
  	}
  }
  ```

### 3、延时消息

- ```java
  public class Test {
  	public static void main(String[] args) throws Exception {
  		DefaultMQProducer producer = new DefaultMQProducer("pg");
  		producer.setNamesrvAddr("rocketmqOS:9876");
  		producer.start();
  		for (int i = 0; i < 10; i++) {
  			byte[] body = ("Hi," + i).getBytes();
  			Message msg = new Message("TopicB", "someTag", body);
  			// 指定消息延迟等级为3级，即延迟10s
  			msg.setDelayTimeLevel(3);
  			SendResult sendResult = producer.send(msg);
  			// 输出消息被发送的时间
  			System.out.print(new SimpleDateFormat("mm:ss").format(new Date()));
  			System.out.println(" ," + sendResult);
  		}
  		producer.shutdown();
  	}
  }
  ```


延迟时间的等级

- ```properties
  messageDelayLevel = 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 1d
  ```


### 4、事务消息

#### 4.1、定义事务监听器

- ```java
  public class MyTransactionListener implements TransactionListener {
  	// 回调操作方法
  	// 消息预提交成功就会触发该方法的执行，用于完成本地事务
  	@Override
  	public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
  		System.out.println("预提交消息成功：" + msg);
  		// 假设接收到TAGA的消息就表示扣款操作成功，TAGB的消息表示扣款失败，
  		// TAGC表示扣款结果不清楚，需要执行消息回查
  		if (StringUtils.equals("TAGA", msg.getTags())) {
  			return LocalTransactionState.COMMIT_MESSAGE;
  		} else if (StringUtils.equals("TAGB", msg.getTags())) {
  			return LocalTransactionState.ROLLBACK_MESSAGE;
  		} else if (StringUtils.equals("TAGC", msg.getTags())) {
  			return LocalTransactionState.UNKNOW;
  		}
  		return LocalTransactionState.UNKNOW;
  	}
  
  	// 消息回查方法
  	// 引发消息回查的原因最常见的有两个：
  	// 1)回调操作返回UNKNWON
  	// 2)TC没有接收到TM的最终全局事务确认指令
  	@Override
  	public LocalTransactionState checkLocalTransaction(MessageExt msg) {
  		System.out.println("执行消息回查" + msg.getTags());
  		return LocalTransactionState.COMMIT_MESSAGE;
  	}
  }
  ```

#### 4.2、定义生产者

- ```java
  public class test {
  	public static void main(String[] args) throws Exception {
  		TransactionMQProducer producer = new TransactionMQProducer("tpg");
  		producer.setNamesrvAddr("rocketmqOS:9876");
  		/**
  		 * 定义一个线程池
  		 * 
  		 * @param corePoolSize    线程池中核心线程数量
  		 * @param maximumPoolSize 线程池中最多线程数
  		 * @param keepAliveTime   这是一个时间。当线程池中线程数量大于核心线程数量 是，* 多余空闲线程的存活时长
  		 * @param unit            时间单位
  		 * @param workQueue       临时存放任务的队列，其参数就是队列的长度
  		 * @param threadFactory   线程工厂
  		 */
  		ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
  				new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
  					@Override
  					public Thread newThread(Runnable r) {
  						Thread thread = new Thread(r);
  						thread.setName("client-transaction-msg-check-thread");
  						return thread;
  					}
  				});
  		// 为生产者指定一个线程池
  		producer.setExecutorService(executorService);
  		// 为生产者添加事务监听器
  		producer.setTransactionListener(new MyTransactionListener());
  		producer.start();
  		String[] tags = { "TAGA", "TAGB", "TAGC" };
  		for (int i = 0; i < 3; i++) {
  			byte[] body = ("Hi," + i).getBytes();
  			Message msg = new Message("TTopic", tags[i], body);
  			// 发送事务消息
  			// 第二个参数用于指定在执行本地事务时要使用的业务参数
  			SendResult sendResult = producer.sendMessageInTransaction(msg, null);
  			System.out.println("发送结果为：" + sendResult.getSendStatus());
  		}
  	}
  
  }
  ```

### 5、消息过滤by sql

- 定义producer

  ```java
  public class test {
  	public static void main(String[] args) throws Exception {
  		DefaultMQProducer producer = new DefaultMQProducer("pg");
  		producer.setNamesrvAddr("rocketmqOS:9876");
  		producer.start();
  		for (int i = 0; i < 10; i++) {
  			try {
  				byte[] body = ("Hi," + i).getBytes();
  				Message msg = new Message("myTopic", "myTag", body);
  				msg.putUserProperty("age", i + "");
  				SendResult sendResult = producer.send(msg);
  				System.out.println(sendResult);
  			} catch (Exception e) {
  				e.printStackTrace();
  			}
  		}
  		producer.shutdown();
  	}
  }
  ```

- 定义consumer

  ```java
  public class test {
  	public static void main(String[] args) throws Exception {
  		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("pg");
  		consumer.setNamesrvAddr("rocketmqOS:9876");
  		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
          //只有push模式才能使用sql过滤
  		consumer.subscribe("myTopic", MessageSelector.bySql("age between 0 and 6"));
  		consumer.registerMessageListener(new MessageListenerConcurrently() {
  			@Override
  			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
  				for (MessageExt me : msgs) {
  					System.out.println(me);
  				}
  				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
  			}
  		});
  		consumer.start();
  		System.out.println("Consumer Started");
  	}
  }
  ```

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

  

