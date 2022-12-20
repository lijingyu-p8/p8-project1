package com.util.demo.cache;

import com.google.common.cache.*;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class GuavaCacheTest {

    public static void main(String[] args) throws ExecutionException {
        initCache();
    }

    private static void initCache() throws ExecutionException {
        LoadingCache<String, Object> loadingCache = CacheBuilder.newBuilder()
                //设置key、value的引用强度
                .softValues()
                .weakKeys()
                .weakValues()
                .maximumSize(100)
                //写入3秒后过期
                .expireAfterWrite(Duration.ofSeconds(3))
                //无访问3秒后过期
                .expireAfterAccess(Duration.ofMinutes(3))
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> removalNotification) {

                    }
                }).build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String s) throws Exception {
                        //当缓存中没有时，通过load进行加载
                        return null;
                    }
                });
        loadingCache.put("sss",new Data());
        loadingCache.get("key1", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //当不存在缓存时，通过Callback回调的方式进行加载
                return null;
            }
        });
        //全部清除
        loadingCache.invalidateAll();
    }
}