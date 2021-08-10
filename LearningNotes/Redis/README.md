# Redis

## 一、Redis安装

```
yum install wget   wget是Linux中的一个下载文件的工具
yum install gcc    安装的是编译c语言的环境 
mkdir soft         创建一个soft目录，用于存放redis相关文件
cd soft            进入到soft目录
wget http://download.redis.io/releases/redis-5.0.5.tar.gz 
                    下载redis
tar xf redis-5.0.5.tar.gz 解压
cd redis-5.0.5     进入redis目录
注意看readme.md
注意：make命令是会调用 Makefile文件
make     执行编译
make install PREFIX=/opt/lijingyu/redis5  将脚本文件安装到指定目录  将文件迁出，和源码分离
cd /opt/lijingyu/  进入到目录 
vi /etc/profile
export REDIS_HOME=/opt/lijingyu/redis5
export PATH=$PATH:$REDIS_HOME/bin
source /etc/profile
进入到redis 的utils 目录
./install_server.sh  默认端口是6379
 cd /etc/init.d  可执行文件会生成到这个目录
 service redis_6379 status  看运行状态
 ps -fe | grep redis  可以查看当前的进程
```

![安装1](images/安装1.png)

![安装2](images/安装2.png)

![安装3](images/安装3.png)

## 二、NIO原理

- 磁盘和数据库索引

  ​        磁盘是4k分页，所以无论取多少数据，每次都是最少4k的读取，所以存在4k对齐现象。数据库数据分页，也是最小单位4k进行分页，索引也是4k分页。将数据和索引维护在磁盘上。在内存中用B+树维护索引和数据的对应关系树状图。

  ​         数据库表很大的情况下，增删改会变慢，因为每次操作都需要重新维护索引。对于查询来讲，单个的或者少量的查询，如果where条件可以命中索引，那么速度依然很快。但是对于并发大的查询，因为会同时命中磁盘上的多条数据，磁盘的访问速度会成为瓶颈，还有网络带宽也会成为瓶颈，最终会影响速度。

  ![NIO](images/NIO.png)

## 三、epoll

![epoll](images/epoll.png)

## 四、五大数据类型

### 4.1、String

1. String类型是二进制安全的。意味着Redis的string可以包含任何数据。比如jpg图片或者序列化的对象。一个Redis中字符串value最多可以是512M。

2. 数据结构：简单动态字符串，内部结构类似java中的ArrayList。会进行动态的扩缩容。采用预分配冗余空间的方式来减少内存的频繁分配。内部为字符串分配的空间大小实际上会比字符串的length大一些。当字符串大小小于1M时，发生扩容时，会翻倍扩容。当大于1M时，扩容会以每次增加1M空间的规则来进行扩容。

   ![](images/string-2.png)

3. 常用命令

   ![](images/string-1.png)

- set key value ：添加键值对

  set key1 value EX 10 NX  当key1不存在时，设置key1,10秒过期。

  NX：当数据库中key不存在时，可以将key-value添加数据库
  XX：当数据库中key存在时，可以将key-value添加数据库，与NX参数互斥
  EX：key的过期秒数
  PX：key的过期毫秒数，与EX互斥

- get key：查询对应键值

- append key value：将给定的value追加到原值的末尾。

- strlen key：获得值的长度。

- setnx  key value：只有在key不存在时，才能设置key的值。

- incr key：将key中储存的数字值增1，只能对数字值操作，如果为空，新增值为1。

- decr key：将 key 中储存的数字值减1，只能对数字值操作，如果为空，新增值为-1

- incrby / decrby  key amount：将key中储存的数字值增减。自定义步长。 incrby k1 10，增加10。

- 批量操作，原子性，有一个失败则都失败。

- mset  key1 value1 key2 value2 ..... ：同时设置一个或多个key-value对 。

- mget  key1 key2 key3 .....：同时获取一个或多个value。  

- msetnx key1 value1 key2 value2 ..... ：同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在时，才会设置成功。

- getrange  key start end：获得值的范围，类似java中的substring，前包，后包。比如value为0123456 那么getrange key 0 1 结果为01，getrange key 2 3结果为23。

- setrange  key offset value：用 value覆写key所储存的字符串值，从offset位置开始(索引从0开始)，进行值的替换。当替换的字符数量超过1时，会对原值从offset开始，批量进行替换，即使超过原值的总长度也可以。

- setex  key seconds value：设置键值的同时，设置过期时间，单位秒。

- getset key value：以新换旧，设置了新值同时获得旧值。

### 4.2、List

1. 简单的字符串列表，类似java中的ArrayList，按照插入顺序排序，并且可以添加一个元素到列表的头或者尾部，也可以从头、尾部移除元素。

2. 内部实现是一个双向链表。

   ![image-20210806170041253](images/list-1.png)

3. 数据结构：

   底层是由quickList+ziplist行程的双向链表。链表的每个节点quickListNode是一个ziplist。

   LinkedList链表每个节点都是一个独立的存储空间，当节点数量比较多的时候，很容易造成空间的不连续，空间浪费。并且当存储的数据很小时，链表的prev、next指针会占用比较大的空间，也会造成空间浪费。所以Redis引入了ziplist压缩列表。

   ziplist 是由一系列特殊编码的内存块构成的列表，表中的每一项都存放在前后连续的内存空间上，每一项因占用的空间不同，而采用变长编码。并且没有prev、next指针，是存储上一个 entry的长度和当前entry的长度，通过长度推算下一个元素在什么地方。牺牲了读取的效率，只适合从头到尾顺序读取，并且字段的值比较小的时候，才适合ziplist。

   quickList是结合了链表、ziplist的有点的链表，每个节点可以看做是一个一个小的ziplist。既满足了快速的插入、删除、读取性能，又不会出现太大的空间冗余。

4. 常用命令：

- lpush/rpush key value1 value2 value3....： 从左边/右边插入一个或多个值。
- lpop/rpop key：从左边/右边弹出一个值，并remove。
- rpoplpush  key1 key2：从key1列表右边弹出一个值，插到key2列表左边。
- lrange key start stop：按照索引下标获得元素(从左到右)，索引下标从0开始，结尾是-1。因为是双向链表，所以正向是从0开始，反向是从-1开始。lrange key1 0 -1，0左边第一个，-1右边第一个，（0，-1表示获取所有）
- lindex key index：按照索引下标获得元素(从左到右)
- llen key：获得列表长度 。
- linsert key BEFORE|AFTER pivot value：在从左边数，第一个pivot（字符匹配项）的左侧插入插入值value，当找不到目标匹配项时，插入失败。
- lrem key count value：从左边删除count个value(从左到右)。
- lset key index value：将列表key下标为index的值替换成value。

### 4.3、Set

1. Redis Set对外提供的功能与list类似，是一个列表的功能，特殊之处在于set是可以自动去重的，当需要存储一个列表数据，又不希望出现重复数据时，set是一个很好的选择，并且set提供了判断某个成员是否在一个set集合内的重要接口，这个也是list所不能提供的。

2. Redis的Set是string类型的无序集合。它底层其实是一个value为null的hash表，所以添加，删除，查找的复杂度都是O(1)。

3. 数据结构：

   Set数据结构是dict字典，字典是用哈希表实现的。
   Java中HashSet的内部实现使用的是HashMap，只不过所有的value都指向同一个对象。Redis的set结构也是一样，它的内部也使用hash结构，所有的value都指向同一个内部值。

4. 常用命令：

- sadd key value1 value2..... ：将一个或多个 member 元素加入到集合 key 中，已经存在的 member 元素将被忽略。
- smembers key：取出该集合的所有值。
- sismember key value：判断集合key是否为含有该value值，有1，没有0。
- scard key：返回该集合的元素个数。
- srem key value1 value2....： 删除集合中的某个元素。
- spop key count：随机从该集合中弹出count个值，并remove。
- srandmember key n：随机从该集合中取出n个值。不会从集合中删除 。
- smove source destination value：把集合中一个值从一个集合移动到另一个集合。
- sinter key1 key2：返回两个集合的交集元素。
- sunion key1 key2：返回两个集合的并集元素。
- sdiff key1 key2：返回两个集合的差集元素(key1中的，不包含key2中的)。

### 4.4、Hash

1. Redis hash 是一个键值对集合。内部存储string类型的field和value的映射表，hash特别适合用于存储对象。类似Java里面的Map<String,Object>。

2. 用户ID为查找的key，存储的value用户对象包含姓名，年龄，生日等信息，如果用普通的key/value结构来存储，主要有以下2种存储方式：

   ![image-20210809124008487](images/hash_1.png)

   ![image-20210809124301343](images/hash_2.png)

3. 数据结构：

   Hash类型对应的数据结构是两种：ziplist（压缩列表），hashtable（哈希表）。当field-value长度较短且个数较少时，使用ziplist，否则使用hashtable。

4. 常用命令：

- hset key field value：给key集合中的 field键赋值value。
- hget key1 field：从key1集合field取出 value。
- hmset key1 field1 value1 field2 value2...：批量设置hash的值。
- hexists key1 field：查看哈希表 key 中，给定域 field 是否存在。 
- hkeys key：列出该hash集合的所有field。
- hvals key：列出该hash集合的所有value。
- hincrby key field increment：为哈希表 key 中的域 field 的值加上增量 1  -1。
- hsetnx key field value：将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在才能设置成功。

### 4.5、Zset

1. Redis有序集合zset与普通集合set非常相似，是一个没有重复元素的字符串集合。不同之处是有序集合的每个成员都关联了一个评分（score）,这个评分（score）被用来按照从最低分到最高分的方式排序集合中的成员。集合的成员是唯一的，但是评分可以是重复了 。

2. 因为元素是有序的, 所以可以很快的根据评分（score）或者次序（position）来获取一个范围的元素。

3. 访问有序集合的中间元素也是非常快的,因此能够使用有序集合作为一个没有重复成员的智能列表。

4. 数据结构：

   SortedSet(zset)是Redis提供的一个非常特别的数据结构，一方面它等价于Java的数据结构Map<String, Double>，可以给每一个元素value赋予一个权重score，另一方面它又类似于TreeSet，内部的元素会按照权重score进行排序，可以得到每个元素的名次，还可以通过score的范围来获取元素的列表。
   zset底层使用了两个数据结构
   （1）hash，hash的作用就是关联元素value和权重score，保障元素value的唯一性，可以通过元素value找到相应的score值。
   （2）跳跃表，跳跃表的目的在于给元素value排序，根据score的范围获取元素列表。

5. 跳表

   ![image-20210810124342740](images/跳表-1.png)

   要查找值为51的元素：

   1、从第2层开始，1节点比51节点小，向后比较。

   2、21节点比51节点小，继续向后比较，后面就是NULL了，所以从21节点向下到第1层

   3、在第1层，41节点比51节点小，继续向后，61节点比51节点大，所以从41向下

   4、在第0层，51节点为要查找的节点，节点被找到，共查找4次。

   如果使用有序链表，需要从链表头开始一次查找比较，查找效率较跳表低。

6. 常用命令：

- ![image-20210810124830915](images/zset-1.png)

- zadd  key score1 value1 score2 value2…：将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
- zrange key start stop  [WITHSCORES] ：返回有序集 key 中，下标在start、stop之间的元素，带WITHSCORES，可以让分数一起和值返回到结果集。
- zrangebyscore key min max [withscores] [limit offset count]：返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。 
- zrevrangebyscore key max min [withscores] [limit offset count]：同上，改为从大到小排列。 
- zincrby key increment value：为元素的score加上增量。
- zrem  key value：删除该集合下，指定值的元素。
- zcount key min max：统计该集合，分数区间内的元素个数。
- zrank key value：返回该值在集合中的排名，从0开始。

### 4.6、key操作

- keys * ：查看当前库所有key
- exists key ：判断某个key是否存在
- type key ：查看key是什么类型
- del key ：删除指定的key数据
- unlink key ：根据key选择非阻塞删除，仅将key从keyspace元数据中删除，真正的删除会在后续异步操作。
- expire key 10 ：10秒钟，为给定的key设置过期时间
- ttl key ：查看还有多少秒过期，-1表示永不过期，-2表示已过期
- select ：切换数据库，select 1 切换到1号库
- dbsize：查看当前数据库的key的数量
- flushdb：清空当前库
- flushall：通杀全部库
