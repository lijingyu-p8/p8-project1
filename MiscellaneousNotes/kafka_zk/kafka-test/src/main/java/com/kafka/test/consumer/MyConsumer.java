package com.kafka.test.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

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
//        while (true) {
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
//            if (!consumerRecords.isEmpty()) {
////                for (ConsumerRecord<String, String> record : consumerRecords) {
////                    //4.打印消息
////                    System.out.printf("收到消息： partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),
////                            record.offset(), record.key(), record.value());
////                }
//                //同步提交
//                //所有的消息已消费完
////                if (consumerRecords.count() > 0) {
////                    // ⼿动同步提交offset，当前线程会阻塞直到offset提交成功
////                    // ⼀般使⽤同步提交，因为提交之后⼀般也没有什么逻辑代码了
////                    consumer.commitSync();//=======阻塞=== 提交成功
////                }
//                //异步提交
//                if (consumerRecords.count() > 0) {
//                    consumer.commitAsync(new OffsetCommitCallback() {
//                        @Override
//                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
//
//                        }
//                    });
//                }
//            }
//        }
        List<PartitionInfo> topicPartitions = consumer.partitionsFor("first-topic");
        //从1⼩时前开始消费
        long fetchDataTime = new Date().getTime() - 1000 * 60 * 60;
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo par : topicPartitions) {
            map.put(new TopicPartition("first-topic", par.partition()), fetchDataTime);
        }
        Map<TopicPartition, OffsetAndTimestamp> parMap =
                consumer.offsetsForTimes(map);
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : parMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndTimestamp value = entry.getValue();
            if (key == null || value == null) continue;
            Long offset = value.offset();
            System.out.println("partition-" + key.partition() + "|offset-" + offset);
            //根据消费⾥的timestamp确定offset
            if (value != null) {
                consumer.assign(Arrays.asList(key));
                consumer.seek(key, offset);
            }
        }
    }
}
