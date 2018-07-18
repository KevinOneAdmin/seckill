#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
#备份原来的资源文件
cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup

cd /etc/yum.repos.d/

#用阿里云或者网易的yum文件覆盖 网易：http://mirrors.163.com/.help/CentOS6-Base-163.repo
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

#行yum makecache生成缓存
yum makecache

#系统更新
yum -y update
