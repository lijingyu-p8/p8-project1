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
