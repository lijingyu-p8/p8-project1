package com.minyu.knowledge.sea.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: TODO
 * @Author: lijingyu
 * @CreateTime: 2023-04-11  21:58
 */
public class LettuceTest {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("key", "Hello, Redis!");
        RedisAsyncCommands<String, String> async = connection.async();
        RedisFuture<String> kkk = async.get("kkk");
//        kkk.thenCompose(
//                data ->{
//                    boolean empty = data.isEmpty();
//                    System.out.println(empty);
//                }
//        );
        connection.close();
        redisClient.shutdown();
        List<RedisURI> redisURIList = new ArrayList<>();
        RedisURI redisUri = RedisURI.Builder.redis("localhost").withPort(6379).withPassword("authentication".toCharArray()).build();
        redisURIList.add(redisUri);
        AsyncConnectionPoolSupport.createBoundedObjectPool()
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisURIList);
        redisClusterClient.setOptions(ClusterClientOptions.builder().topologyRefreshOptions(ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh()..build()).build());
        StatefulRedisClusterConnection<String, String> connect = redisClusterClient.connect();
        RedisAdvancedClusterAsyncCommands<String, String> async1 = connect.async();
    }
}