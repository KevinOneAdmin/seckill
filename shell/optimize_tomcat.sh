#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
cd /home/work/tomcat-8/conf/
vi service.xml
#Connector节点
#最大链接数
maxConnections = 10000
#等待队列最大值
acceptCount = 100
#最大线程数
maxThreads = 200
#最小等待线程
minSpareThreads = 200

#动态更新项目 host节点
autoDeploy = false
#DNS查询 HTTP节点
enableLookups =fasle
#热更行 context节点
reloadable =false