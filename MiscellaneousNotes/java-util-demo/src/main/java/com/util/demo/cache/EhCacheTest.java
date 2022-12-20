package com.util.demo.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class EhCacheTest {
    public static void main(String[] args) {
        iniCache();
        iniCache2();
    }

    private static void iniCache2() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();
        cacheManager.init();

        Cache<Long, String> preConfigured =
                cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)));

        myCache.put(1L, "da one!");
        String value = myCache.get(1L);

        cacheManager.removeCache("preConfigured");

        cacheManager.close();
    }

    private static void iniCache() {
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder()
                //数量
                .heap(10, EntryUnit.ENTRIES);
        //占用大小
//                .heap(10, MemoryUnit.MB)
        //磁盘文件大小
        // 磁盘存储,记得buld添加true，才能正常的持久化，并且序列化以及反序列化
//                .disk(100, MemoryUnit.MB);
        CacheConfigurationBuilder<String, Data> stringDataCacheConfigurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Data.class, resourcePoolsBuilder.build())
                //空闲回收时间，无访问之后，多久删除缓存，一直有访问则会自动进行续期
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.of(10, ChronoUnit.SECONDS)));
        //存活时间，设置固定失效时间，即使一直有访问，也不会自动续期
        //.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(10, ChronoUnit.SECONDS)))
        //不设置过期时间
        //.withExpiry(ExpiryPolicy.NO_EXPIRY);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("cache01", stringDataCacheConfigurationBuilder)
                .with(CacheManagerBuilder.persistence("D://1.txt"))
                .build(true);
        Cache<String, Data> cache = cacheManager.getCache("cache01", String.class, Data.class);
        cache.put("01", new Data());
        Data data = cache.get("01");
        System.out.println(data);
        cacheManager.close();
    }
}