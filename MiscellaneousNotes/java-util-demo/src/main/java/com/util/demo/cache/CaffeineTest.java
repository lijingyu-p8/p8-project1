package com.util.demo.cache;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CaffeineTest {
    public static void main(String[] args) throws InterruptedException {
        initCache();
        initCacheByExpireVariably();
    }

    private static void initCache() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                //写入后5秒过期
                .expireAfterWrite(Duration.ofSeconds(5))
                //无访问5秒后过期，多个过期策略只能使用其中一个
                .expireAfterAccess(Duration.ofSeconds(5))
                .maximumSize(10)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(@Nullable String s, @Nullable Object o, @NonNull RemovalCause removalCause) {

                    }
                }).softValues()
                .recordStats()
                .build(new CacheLoader<String, Object>() {//loader
                    @Override
                    public @Nullable Object load(@NonNull String s) throws Exception {
                        return null;
                    }
                });
        System.out.println(System.currentTimeMillis());
        cache.put("444", "444");
        cache.put("444", "555");
        System.out.println(cache.getIfPresent("222"));
        //回调
        cache.get("key1", new Function<String, Object>() {
            @Override
            public Object apply(String s) {
                return null;
            }
        });
    }

    /**
     * 支持不同key设置不同的过期时间
     */
    private static void initCacheByExpireVariably() throws InterruptedException {
        Cache<String, Object> cache = Caffeine.newBuilder()
//                .expireAfterWrite(Duration.ofSeconds(5))
                .maximumSize(10)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(@Nullable String s, @Nullable Object o, @NonNull RemovalCause removalCause) {

                    }
                }).softValues()
                .recordStats()
                .expireAfter(new Expiry<String, Object>() {
                    @Override
                    public long expireAfterCreate(@NonNull String s, @NonNull Object o, long l) {
                        System.out.println(l);
                        return l;
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull String s, @NonNull Object o, long l, @NonNegative long l1) {
                        return l1;
                    }

                    @Override
                    public long expireAfterRead(@NonNull String s, @NonNull Object o, long l, @NonNegative long l1) {
                        return l1;
                    }
                })
                .build();
        cache.policy().expireVariably().ifPresent(policy -> {
            policy.put("111", "111", Duration.ofSeconds(5));
        });
        cache.policy().expireVariably().ifPresent(policy -> {
            policy.put("222", "222", Duration.ofSeconds(1));
        });
        cache.policy().expireVariably().ifPresent(policy -> {
            policy.put("333", "333", Duration.ofSeconds(2));
        });
        System.out.println(System.currentTimeMillis());
//        cache.put("444","444");
//        cache.put("444","555");
        System.out.println(cache.getIfPresent("222"));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.getIfPresent("111"));
        System.out.println(cache.getIfPresent("333"));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.getIfPresent("111"));
        System.out.println(cache.getIfPresent("222"));
        System.out.println(cache.getIfPresent("333"));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.getIfPresent("111"));
        System.out.println(cache.getIfPresent("222"));
        System.out.println(cache.getIfPresent("333"));
    }
}