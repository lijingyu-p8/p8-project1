# RocketMQ安装

## 一、单机安装

- ```bash
  #1、创建目录
  mkdir /usr/local/soft
  #2、进入目录
  cd /usr/local/soft
  #3、下载
  wget https://archive.apache.org/dist/rocketmq/4.9.0/rocketmq-all-4.9.0-bin-release.zip
  #4、解压
  unzip rocketmq-all-4.9.0-bin-release.zip
  #5、重命名
  mv rocketmq-all-4.9.0-bin-release rocketmq-4.9.0
  #6、进入目录
  cd rocketmq-4.9.0
  #7.修改初始内存
  vim bin/runserver.sh
  #将虚拟机配置减小
  -Xms512m -Xmx512m -Xmn128m
  vim bin/runbroker.sh
  #将虚拟机配置减小
  -Xms512m -Xmx512m -Xmn128m
  #8、启动nameserver
  bin/mqnamesrv
  #9、启动broker
  bin/mqbroker -n localhost:9876 #连接nameserver 9876
  #10、关闭
  bin/mqshutdown nameserver  bin/mqshutdown namesrv
  ```

- 修改初始内存

  默认的启动参数虚拟机内存设置过大，不适合平时demo练习

  ![](images/默认启动参数-1.png)

- 修改后

  ![](images/修改后启动参数-1.png)

- 启动nameserver

  ![](images/启动nameserver-1.png)

- 启动broker

  ![](images/启动broker-1.png)

## 二、dashboard安装

