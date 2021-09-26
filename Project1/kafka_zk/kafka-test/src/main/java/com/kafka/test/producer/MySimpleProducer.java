package com.kafka.test.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

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
