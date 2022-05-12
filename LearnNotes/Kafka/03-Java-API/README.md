# Java-API

## 一、生产者

### 1.1、同步发送消息

```java
public class MySimpleProducer {
    private final static String TOPIC_NAME = "first-topic";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.设置参数
        Properties properties = new Properties();
        //设置Kafka地址
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.73.101:9092,192.168.73.102:9092,192.168.73.103:9092");
        //把发送的key从字符串序列化为字节数组
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //把发送消息value从字符串序列化为字节数组
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //2.创建⽣产消息的客户端，传⼊参数
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        //key：作⽤是决定了往哪个分区上发， value：具体要发送的消息内容
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, "mykeyvalue", "hellokafka");
        //4.发送消息,得到消息发送的元数据并输出
        RecordMetadata metadata = producer.send(producerRecord).get();
        System.out.println("同步⽅式发送消息结果： " + "topic-" +
                metadata.topic() + "|partition-" + metadata.partition() + "|offset-" + metadata.offset());
    }
}
```

### 1.2、发送到指定的分区

```java
ProducerRecord<String, String> partisionRecord = new ProducerRecord<>(TOPIC_NAME, 0, "key1", "value1");
```

### 1.3、异步发送消息

```java
producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.println("异步⽅式发送消息结果： " + "topic-" +
                        recordMetadata.topic() + "|partition-" + recordMetadata.partition() + "|offset-" + recordMetadata.offset());
            }
        });
```

### 1.4、ack配置

1. acks=0： producer 不等待 broker 的 ack，这一操作提供了一个最低的延迟， broker 一接收到将消息写入pagecache中，但是还没有写入磁盘就已经返回，当 broker 故障时有可能丢失数据。
2. acks=1： ⾄少要等待leader已经成功将数据写⼊本地log，但是不需要等待所有follower是否成功写⼊。就可以继续发送下⼀
   条消息。这种情况下，如果follower没有成功备份数据，⽽此时leader⼜挂掉，则消息会丢失。
3. acks=-1或all： 需要等待 min.insync.replicas(默认为1，推荐配置⼤于等于2) 这个参数配置的副本个数都成功写⼊⽇志，这种策略会保证只要有⼀个备份存活就不会丢失数据。这是最强的数据保证。

```java
properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
/*
发送失败会重试，默认重试间隔100ms，重试能保证消息发送的可靠性，但是也可能造
成消息重复发送，⽐如⽹络抖动，所以需要在
接收者那边做好消息接收的幂等性处理
*/
properties.put(ProducerConfig.RETRIES_CONFIG, 3);
//重试间隔设置
properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 300);
```

## 二、消费者

### 1.1、消费消息

```java
public class MyConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.73.101:9092,192.168.73.102:9092,192.168.73.103:9092");
        // 消费分组名
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //1.创建⼀个消费者的客户端
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("first-topic"));
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            if (!consumerRecords.isEmpty()) {
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    //4.打印消息
                    System.out.printf("收到消息： partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),
                            record.offset(), record.key(), record.value());
                }
            }
        }
    }
}
```

### 2.2、同步提交offset

```java
//关闭自动提交
props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//所有的消息已消费完
if (consumerRecords.count() > 0) {
    // ⼿动同步提交offset，当前线程会阻塞直到offset提交成功
    // ⼀般使⽤同步提交，因为提交之后⼀般也没有什么逻辑代码了
    consumer.commitSync();//=======阻塞=== 提交成功
}
```

### 2.3、异步提交offset

```java
//异步提交
if (consumerRecords.count() > 0) {
    consumer.commitAsync(new OffsetCommitCallback() {
        @Override
        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

        }
    });
}
```

