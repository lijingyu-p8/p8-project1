# 一、安装docker

```bash
#1.卸载之前的docker
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
#2.设置存储库
sudo yum install -y yum-utils
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
#3.安装docker引擎
sudo yum install docker-ce docker-ce-cli containerd.io
#4.开机启动
sudo systemctl enable docker
```

配置阿里云镜像

```bash
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://rowhrf9a.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

# 二、安装es

```bash
#拉取es镜像
docker pull elasticsearch:7.17.3
#创建本地目录
mkdir /usr/local/soft/dockersoft/mydata/elasticsearch/node01/config
mkdir /usr/local/soft/dockersoft/mydata/elasticsearch/node01/logs
mkdir /usr/local/soft/dockersoft/mydata/elasticsearch/node01/data
mkdir /usr/local/soft/dockersoft/mydata/elasticsearch/node01/plugins
#设置权限，任何用户都有读写权限，保证docker运行成功
chmod -R 777 /usr/local/soft/
```

config配置

```bash
config目录下创建elasticsearch.yml
#可以被远程任意机器访问
http.host: 0.0.0.0
//节点名称
node.name: node-01
//集群名称
cluster.name: my-application
```

启动

```bash
docker run --name elasticsearch7.17.3-node01 -p 19200:9200 -p 19300:9300 \
-e  "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-v /usr/local/soft/dockersoft/mydata/elasticsearch/node01/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /usr/local/soft/dockersoft/mydata/elasticsearch/node01/data:/usr/share/elasticsearch/data \
-v /usr/local/soft/dockersoft/mydata/elasticsearch/node01/logs:/usr/share/elasticsearch/logs \
-v /usr/local/soft/dockersoft/mydata/elasticsearch/node01/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.17.3
```

自启动

```bash
docker update elasticsearch7.17.3-node01 --restart=always
```

# 三、安装kibana

```bash
docker pull kibana:7.17.3

docker run --name kibana-7.17.3 -e ELASTICSEARCH_HOSTS=http://192.168.152.128:19200 -p 15601:5601 -d kibana:7.17.3
```

