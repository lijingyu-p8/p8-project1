# Logstash入门简介

## 介绍

Logstash是一个开源的服务器端数据处理管道，能够同时从多个来源采集数据，转换数据，然后将数据发送到最喜欢的存储库中（我们的存储库当然是ElasticSearch）

![image-20210829105222260](images/介绍.png)

我们回到我们ElasticStack的架构图，可以看到Logstash是充当数据处理的需求的，当我们的数据需要处理的时候，会将它发送到Logstash进行处理，否则直接送到ElasticSearch中。

![image-20200924213319642](images/image-20200924213319642.png)

## 用途

Logstash可以处理各种各样的输入，也可以做格式转换、filter，处理完后发送到ElasticSearch。

![image-20210829110057944](images/介绍-1.png)

## 部署安装

Logstash主要是将数据源的数据进行一行一行的处理，同时还具有过滤切割等功能。

![image-20200924214152859](images/image-20200924214152859.png)

首先到官网下载logstash：https://www.elastic.co/cn/downloads/logstash

选择我们需要下载的版本：

![image-20210828173902479](images/下载-1.png)

```bash
#创建文件夹
mkdir /usr/local/soft/logstash
#下载
wget https://artifacts.elastic.co/downloads/logstash/logstash-7.13.2-linux-x86_64.tar.gz
#解压安装包
tar -xaf logstash-7.13.2-linux-x86_64.tar.gz
#重命名
mv logstash-7.13.2 logstash
#第一个logstash示例
bin/logstash -e 'input { stdin { } } output { stdout {} }'
```

其实原来的logstash的作用，就是为了做数据的采集，但是因为logstash的速度比较慢，所以后面使用beats来代替了Logstash，当我们使用上面的命令进行启动的时候，就可以发现了，因为logstash使用java写的，首先需要启动虚拟机，最后下图就是启动完成的截图。启动过程比较缓慢。

![image-20210828174417763](images/logstash-启动-1.png)

## 测试

我们在控制台输入 hello，马上就能看到它的输出信息

![image-20210828174524695](images/hello.png)

## 配置详解

Logstash的配置有三部分，如下所示

```bash
input { #输入
stdin { ... } #标准输入
}
filter { #过滤，对数据进行分割、截取等处理
...
}
output { #输出
stdout { ... } #标准输出
}
```

### 输入

- 采集各种样式、大小和来源的数据，数据往往以各种各样的形式，或分散或集中地存在于很多系统中。

- Logstash 支持各种输入选择 ，可以在同一时间从众多常用来源捕捉事件。能够以连续的流式传输方式，轻松地从日志、指标、Web 应用、数据存储以及各种 AWS 服务采集数据。

  ![image-20210829105107860](images/输入.png)

### 过滤

- 实时解析和转换数据

  ![image-20210829104746286](images/过滤.png)

- 数据从源传输到存储库的过程中，Logstash 过滤器能够解析各个事件，识别已命名的字段以构建结构，并将它们转换成通用格式，以便更轻松、更快速地分析和实现商业价值。

- 支持的过滤插件

  ```http
  https://www.elastic.co/guide/en/logstash/7.13/filter-plugins.html
  ```

  ![image-20210829103532233](images/过滤插件-1.png)

- grok插件示例

  IP、WORD、NUMBER、均为内置的正则比配规则。

  IP:client：匹配到ip内容，赋值给client字段。

  ```bash
  input {
        stdin{}
      }
      filter {
        grok {
          match => { "message" => "%{IP:client} %{WORD:method} %{URIPATHPARAM:request} %{NUMBER:bytes} %{NUMBER:duration}" }
        }
      }
  output {
          stdout { codec => rubydebug }
  }
  ```

- 输入测试数据

  ```
  55.3.244.1 GET /index.html 15824 0.043
  ```

  进行字段解析，并赋值到指定字段。

  ![image-20210829104455008](images/grok-1.png)

### 输出

Logstash提供众多输出选择，可以将数据发送到要指定的地方，并且能够灵活地解锁众多下游用例。

![image-20210829104912179](images/输出.png)

## 读取自定义日志

前面通过Filebeat读取了nginx的日志，如果是自定义结构的日志，就需要读取处理后才能使用，所以，这个时候就需要使用Logstash了，因为Logstash有着强大的处理能力，可以应对各种各样的场景。

### 日志结构

```bash
2021-08-28 21:21:21|ERROR|1 读取数据出错|参数：id=1002
```

可以看到，日志中的内容是使用“|”进行分割的，使用，我们在处理的时候，也需要对数据做分割处理。

### 编写配置文件

```bash
vim my_pipeline.conf
```

然后添加如下内容

```bash
input {
    file {
        path => "/usr/local/soft/testlog/log2.log"
        start_position => "beginning"
    }
}
filter {
    mutate {
    	split => {"message"=>"|"}
    }
}
output {
	stdout { codec => rubydebug }
}
```

启动

```bash
#启动
./bin/logstash -f ./config/my_pipelines.yml
```

然后我们就插入我们的测试数据

```bash
echo "2021-08-28 21:21:21|ERROR|读取数据出错|参数：id=1002" >> log2.log
```

然后我们就可以看到logstash就会捕获到刚刚我们插入的数据，同时我们的数据也被分割了

![image-20210828180319197](images/filter-1.png)

### 输出到Elasticsearch

我们可以修改我们的配置文件，将我们的日志记录输出到ElasticSearch中

```bash
#默认的官方示例配置
conf/logstash-sample.conf
```

```bash
input {
  beats {
    port => 5044 #默认开5044端口，用于beats上传数据
  }
}

output {
  elasticsearch {
    hosts => ["http://192.168.73.90:9200"]
    index => "logstash-test-%{+YYYY.MM.dd}" #自定义日志名称
    #user => "elastic" #如果es需要用户名、秘密
    #password => "changeme"
  }
}
```

然后在重启我们的logstash

```bash
./bin/logstash -f ./logstash-sample.conf
```

![image-20210828211428126](images/连接es-1.png)

beats读取Linux系统日志，发送到logstash

最后就能够看到我们刚刚插入的数据了

![image-20210828211713375](images/输出到es-kibana-1.png)

![image-20210828211815422](images/输出到es-kibana-2.png)