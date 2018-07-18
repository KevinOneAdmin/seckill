#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
#卸载openjdk
#获取open-jdk信息
rpm -qa | grep java
#卸载JDK包
rpm -e --nodeps XXXX_openjdk_XXX
#或者全部卸载
rpm -e --nodeps `rpm -qa | grep java`

#安装JDK
#上传jdk
scp ~/jdk-8u172-linux-x64.tar.gz kevin@192.168.56.101:/home/work
#切换用户
su root
cd /home/work
#解压JDK
tar -xzvf jdk-8u172-linux-x64.tar.gz
cd jdk1.8.0_172
pwd

#设置环境变量
vi /etc/profile
export JAVA_HOME=/home/work/jdk1.8.0_172
export PATH=$PATH:$JAVA_HOME/bin
#生效环境变量
source /etc/profile

#验证
java -version
