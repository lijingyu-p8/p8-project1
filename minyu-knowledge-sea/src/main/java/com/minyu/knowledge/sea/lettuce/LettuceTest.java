package com.minyu.knowledge.sea.lettuce;

import io.lettuce.core.RedisClient;
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
import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.batch.BatchExecutor;
import io.lettuce.core.dynamic.batch.BatchSize;
import io.lettuce.core.dynamic.batch.CommandBatching;

import java.util.*;
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
//        AsyncConnectionPoolSupport.createBoundedObjectPool()
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisURIList);
        redisClusterClient.setOptions(ClusterClientOptions.builder().topologyRefreshOptions(ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh().build()).build());
        StatefulRedisClusterConnection<String, String> connect = redisClusterClient.connect();
        RedisAdvancedClusterAsyncCommands<String, String> async1 = connect.async();
    }

    public void testPipelineBatch(RedisClusterClient redisClusterClient) {
        RedisCommandFactory factory = new RedisCommandFactory(redisClusterClient.connect());
        StringCommands commands = factory.getCommands(StringCommands.class);
        // queued until 50 command invocations reached.
        // The 50th invocation flushes the queue.
        commands.set("key", "value");

        // invocation-level queueing control
        commands.get("key", CommandBatching.queue());
        // invocation-level queueing control,
        // flushes all queued commands
        commands.get("key", CommandBatching.flush());

        //异步
        StringCommands2 commands2 = factory.getCommands(StringCommands2.class);

        commands2.flush(); // force-flush

    }

    @BatchSize(50)
    interface StringCommands extends Commands {

        void set(String key, String value);

        RedisFuture<String> get(String key);

        RedisFuture<String> get(String key, CommandBatching batching);
    }

    @BatchSize(50)
    interface StringCommands2 extends Commands, BatchExecutor {
        RedisFuture<String> get(String key);
    }

    public void testBatch(RedisClusterClient redisClusterClient) {
        // service 中持有 factory 实例，只创建一次。第二个参数表示 key 和 value 使用 byte[] 编解码
        RedisCommandFactory factory = new RedisCommandFactory(redisClusterClient.connect(), Arrays.asList(ByteArrayCodec.INSTANCE, ByteArrayCodec.INSTANCE));
        // 使用的地方，创建一个查询实例代理类调用命令，最后刷入命令
        List<RedisFuture<?>> futures = new ArrayList<>();
        RedisBatchQuery batchQuery = factory.getCommands(RedisBatchQuery.class);
        futures.add(batchQuery.get("a".getBytes()));
        futures.add(batchQuery.get("b".getBytes()));
        futures.add(batchQuery.get("c".getBytes()));
        futures.add(batchQuery.get("d".getBytes()));
        // 异步命令调用完成后执行 flush 批量执行，此时命令才会发送给 Redis 服务端
        batchQuery.flush();
        CompletableFuture[] array = futures.toArray(new CompletableFuture[]{});
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(array);
        voidCompletableFuture.join();
        //java.util.concurrent.CompletableFuture.join
        voidCompletableFuture.thenApply((Void) -> {
            for (int i = 0; i < futures.size(); i++) {
                RedisFuture<?> redisFuture = futures.get(i);
//                Object o = redisFuture.get();
            }
            return "";
        });
    }

    /**
     * 定义会用到的批量命令
     */
    @BatchSize(100)
    public interface RedisBatchQuery extends Commands, BatchExecutor {

        RedisFuture<byte[]> get(byte[] key);

        RedisFuture<Set<byte[]>> smembers(byte[] key);

        RedisFuture<List<byte[]>> lrange(byte[] key, long start, long end);

        RedisFuture<Map<byte[], byte[]>> hgetall(byte[] key);
    }
}