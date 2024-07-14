package com.minyu.knowledge.sea.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TimestampConsumer {

    public static void main(String[] args) {
        // 设置消费者配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group-timestamp");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // 指定要消费的topic和分区
            String topic = "your-topic";
            int partition = 0;

            // 指定时间戳（毫秒）
            long timestamp = Instant.now().minusSeconds(60).toEpochMilli(); // 例如，从现在向前推60秒

            // 查找该时间戳对应的偏移量
            Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
            timestampsToSearch.put(new TopicPartition(topic, partition), timestamp);

            Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(timestampsToSearch);

            if (offsets.containsKey(new TopicPartition(topic, partition))) {
                OffsetAndTimestamp offsetAndTimestamp = offsets.get(new TopicPartition(topic, partition));
                if (offsetAndTimestamp != null) {
                    long offset = offsetAndTimestamp.offset();

                    // 分配分区
                    consumer.assign(Collections.singletonList(new TopicPartition(topic, partition)));

                    // 寻求到指定偏移量
                    consumer.seek(new TopicPartition(topic, partition), offset);

                    // 循环消费消息
                    while (true) {
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.printf("offset = %d, timestamp = %d, key = %s, value = %s%n",
                                    record.offset(), record.timestamp(), record.key(), record.value());
                        }

                        // 根据需要处理偏移量提交
                        // consumer.commitSync();

                        // 根据需要添加退出条件
                    }
                } else {
                    System.out.println("No offset found for the given timestamp.");
                }
            } else {
                System.out.println("Failed to find offsets for the given topic-partition.");
            }
        }
    }
}