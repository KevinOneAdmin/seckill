#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
#上传tomcat
scp ~/apache-tomcat-8.5.32.tar.gz kevin@192.168.56.101:/home/work
#解压
tar -zxvf apache-tomcat-8.5.32.tar.gz
mv apache-tomcat-8.5.32 tomcat-8
cd ./tomcat-8
chmod 777 ./bin
#添加防火墙端口
su root
firewall-cmd --zone=public --add-port=8080/tcp --permanent
firewall-cmd --reload
#测试
sh ./bin/startup.sh
#访问localhost:8080