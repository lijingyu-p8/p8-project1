# Kafka安装&集群搭建

```bash
#1.创建目录
mkdir /usr/local/soft/kafka
#2.下载Kafka
wget https://dlcdn.apache.org/kafka/2.7.1/kafka_2.12-2.7.1.tgz
#3.解压
tar -xvf kafka_2.12-2.7.1.tgz
#4.重命名
mv kafka_2.12-2.7.1 kafka-2.7.1
#5./usr/local/soft/kafka目录下，创建日志目录
mkdir logs  
#6.进入目录
cd /usr/local/soft/kafka/kafka-2.7.1/config
#7.编辑配置文件
vim server.properties
#broker 的全局唯一编号，不能重复
broker.id=0
#kafka 运行日志存放的路径
log.dirs=/usr/local/soft/kafka/logs
#配置连接 Zookeeper 集群地址
zookeeper.connect=192.168.73.101:2181,192.168.73.102:2181,192.168.73.103:2181
#8.配置环境变量
vim /etc/profile
#增加配置
export KAFKA_HOME=/usr/local/soft/kafka/kafka-2.7.1
export PATH=:$PATH:${KAFKA_HOME}
#9.启动集群
./bin/kafka-server-start.sh  ./config/server.properties
#10.检验启动是否成功
./bin/kafka-topics.sh bootstrap-server 192.168.73.101:9092 --list #查看topics
```

- etc-profile

  ![](images/etc-profile.png)

- 启动成功

  ![](images/启动成功-1.png)

