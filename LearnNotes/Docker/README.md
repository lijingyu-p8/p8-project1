# 一、docker在线安装

## 1.1、安装

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

## 1.2、设置阿里云镜像加速

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

# 二、docker离线安装

当服务器不能联网的时候，特别是在公司内部服务器安装docker，需要离线安装docker。

通过地址https://download.docker.com/linux/static/stable/x86_64/下载docker离线安装文件

```shell
docker-20.10.9.tgz 
#解压docker二进制文件
tar xzvf /path/to/<FILE>.tar.gz
#复制docker文件
sudo cp docker/* /usr/bin/
#目录下创建docker.service文件
/etc/systemd/system/
#添加文件权限
chmod +x /etc/systemd/system/docker.service
#启动守护进程
systemctl daemon-reload
#启动docker服务
systemctl start docker
#设置开机自启
systemctl enable docker.service
#查看docker版本
docker -v
#运行hello world进行测试
sudo docker run hello-world
```

也可执行脚本文件install.sh，一键离线安装

```sh
#!/bin/sh
echo '解压tar包...'
tar -xvf $1
echo '将docker目录移到/usr/bin目录下...'
cp docker/* /usr/bin/
echo '将docker.service 移到/etc/systemd/system/ 目录...'
cp docker.service /etc/systemd/system/
echo '添加文件权限...'
chmod +x /etc/systemd/system/docker.service
echo '重新加载配置文件...'
systemctl daemon-reload
echo '启动docker...'
systemctl start docker
echo '设置开机自启...'
systemctl enable docker.service
echo 'docker安装成功...'
docker -v
```

docker.service文件，将docker注册成系统文件

```sh
[Unit]
Description=Docker Application Container Engine
Documentation=https://docs.docker.com
After=network-online.target firewalld.service
Wants=network-online.target

[Service]
Type=notify
# the default is not to use systemd for cgroups because the delegate issues still
# exists and systemd currently does not support the cgroup feature set required
# for containers run by docker
ExecStart=/usr/bin/dockerd
ExecReload=/bin/kill -s HUP $MAINPID
# Having non-zero Limit*s causes performance problems due to accounting overhead
# in the kernel. We recommend using cgroups to do container-local accounting.
LimitNOFILE=infinity
LimitNPROC=infinity
LimitCORE=infinity
# Uncomment TasksMax if your systemd version supports it.
# Only systemd 226 and above support this version.
#TasksMax=infinity
TimeoutStartSec=0
# set delegate yes so that systemd does not reset the cgroups of docker containers
Delegate=yes
# kill only the docker process, not all processes in the cgroup
KillMode=process
# restart the docker process if it exits prematurely
Restart=on-failure
StartLimitBurst=3
StartLimitInterval=60s

[Install]
WantedBy=multi-user.target
```

卸载脚本 uninstall.sh

```sh
#!/bin/sh
echo '删除docker.service...'
rm -f /etc/systemd/system/docker.service
echo '删除docker文件...'
rm -rf /usr/bin/docker*
echo '重新加载配置文件'
systemctl daemon-reload
echo '卸载成功...'
```

# 三、docker常用命令

## 3.1、帮助启动类命令

```sh
#启动docker
systemctl start docker
#停止docker： 
systemctl stop docker
#重启docker： 
systemctl restart docker
#查看docker状态
systemctl status docker
#开机启动
systemctl enable docker
#查看docker概要信息
docker info
#查看docker总体帮助文档
docker --help
```

## 3.2、镜像命令
