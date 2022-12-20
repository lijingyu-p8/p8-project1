## Guava Cache

Guava Cache 是一个比较基础的本地缓存工具，提供了JVM缓存管理功能。

### 1、Guava Cache 的优势

- 缓存过期和淘汰机制

  在GuavaCache中可以设置Key的过期时间，包括访问过期和创建过期

  GuavaCache在缓存容量达到指定大小时，采用LRU的方式，将不常使用的键值从Cache中删除

- 并发处理能力

  GuavaCache类似CurrentHashMap，是线程安全的。提供了设置并发级别的api，使得缓存支持并发的写入和读取

  采用分离锁机制，分离锁能够减小锁力度，提升并发能力，分离锁是分拆锁定，把一个集合看分成若干partition,，每个partiton一把锁。ConcurrentHashMap就是分了16个区域，这16个区域之间是可以并发的。GuavaCache采用Segment做分区。

- 更新锁定

  一般情况下，在缓存中查询某个key，如果不存在，则查源数据，并回填缓存。（Cache Aside Pattern）在高并发下会出现，多次查源并重复回填缓存，可能会造成源的宕机（DB），性能下降，GuavaCache可以在CacheLoader的load方法中加以控制，对同一个key，只让一个请求去读源并回填缓存，其他请求阻塞等待。

- 集成数据源

  一般我们在业务中操作缓存，都会操作缓存和数据源两部分，GuavaCache的get可以集成数据源，在从缓存中读取不到时可以从数据源中读取数据并回填缓存

- 可以设置key移除监听事件

  通过removalListener，可以设置key移除事件，当某个key被移除时，可以得到通知。

- 可以通过CacheLoader、Callback的形式，加载数据

- 可以设置value、key的引用强度，当内存不足时，优先回收缓存

### 2、代码示例

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.1-jre</version>
</dependency>
```



```java
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
```

