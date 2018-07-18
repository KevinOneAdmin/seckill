#!/usr/bin/env bash

echo "运行在LVS主机"
echo 1 > /proc/sys/net/ipv4/ip_forward
ipv=/sbin/ipvsadm
vip=192.168.56.102
rs1=192.168.56.103
rs2=192.168.56.104
case $1 in
start)
    echo "Start LVS"
    #添加虚拟网卡
    ifconfig eth0:0 $vip broadcast $vip netmask 255.255.255.255 up
    #添加到虚拟主机的路由
    route add -host $vip dev eth0:0
    #添加虚拟服务器, -s:调度算法
    $vip -A -t $vip:80 -s lc
    #添加真实服务器地址 -g:DR ,-w:权重
    $vip -a -t $vip:80 -r $rs1:80 -g -w -1
    $vip -a -t $vip:80 -r $rs2:80 -g -w -1
;;
stop)
    echo "Stop LVS"
    #删除虚拟网卡
    route del -host $vip dev eth0:0
    #删除路由
    ifconfig eth0:0 down
    #删除虚拟主机
    $ipv -c
;;
*)
echo "Usage:$0 {start|stop}"
exit 1
esac