#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
#环境准备
yum install gcc
yum install gcc-c++
yum install make
yum -y install autoconf // 安装autoconf
yum -y install libtool // 安装libtool

#openssl安装
#查看现在版本
openssl version -a
#产看安装包
rpm -qa openssl
#卸载 OpenSSL
rpm -e --allmatches --nodeps openssl-1.0.1e-60.el7_3.1.x86_64
#下载最新的
wget https://www.openssl.org/source/openssl-1.0.2h.tar.gz
#更新zlib
yum install -y zlib
#解压安装
tar zxf openssl-1.0.2h.tar.gz
cd openssl-1.0.2h
./config shared zlib
make
make install
#更新本地
mv /usr/bin/openssl /usr/bin/openssl.bak
mv /usr/include/openssl /usr/include/openssl.bak
ln -s /usr/local/ssl/bin/openssl /usr/bin/openssl
ln -s /usr/local/ssl/include/openssl /usr/include/openssl
echo "/usr/local/ssl/lib" >> /etc/ld.so.conf
ldconfig -v
#或者不用卸载直接yum install -y openssl



#apr安装如果有可以不用安装直接安装Tomcat-native
rpm -qa|grep  apr
#yum安装方式
yum install apr-devel apr apr-util
#然后卸载安装tomcat-native
rpm -qa|grep tomcat-native
rpm -e --allmatches --nodeps tomcat-native1.2.33.... #卸载自带的native
#cd 到 tomcat bin目录下去解压安装，

#安装apr
wget http://mirror.bit.edu.cn/apache/apr/apr-1.5.2.tar.gz
tar -xvf apr-1.5.2.tar.gz
cd apr-1.5.2
#制定编译后的目录 可能会出现错误：rm: cannot remove `libtoolT': No such file or directory
#解决办法是用vim打开configure
#vim configure
#在30206行左右 把这句$RM    "$cfgfile" 注释掉（前面加#）
./configure --prefix=/usr/local/apr
make
make install

#安装apr-iconv
cd /root/software
wget http://mirror.bit.edu.cn/apache/apr/apr-iconv-1.2.1.tar.gz
tar -xvf apr-iconv-1.2.1.tar.gz
cd apr-iconv-1.2.1
./configure --prefix=/usr/local/apr-iconv --with-apr=/usr/local/apr
make
make install

#安装apr-util
wget http://mirror.bit.edu.cn/apache/apr/apr-util-1.5.4.tar.gz
cd apr-util-1.5.4
./configure --prefix=/usr/local/apr-util  --with-apr=/usr/local/apr  --with-apr-iconv=/usr/local/apr-iconv/bin/apriconv
make
make install

#安装tomcat-native
cd /home/tomcat-8/bin
tar xvf tomcat-native.tar.gz
cd tomcat-native-1.2.4-src/native
./configure --with-apr=/usr/local/apr
make
make install

#设置apr的环境变量
vi  ../../bin/catalina.sh
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/apr/lib
export LD_LIBRARY_PATH

#使用apr
cd ../conf/
vi server.xml
#修改如下
<Connector port="8080"
        protocol="org.apache.coyote.http11.Http11AprProtocol"
        executor="tomcatThreadPool"
        compression="on"
        compressionMinSize="2048"
        maxThreads="30000"
        minSpareThreads="512"
        maxSpareThreads="2048"
        enableLookups="false"
        redirectPort="8443"
        acceptCount="35000"
        debug="0"
        connectionTimeout="40000"
        disableUploadTimeout="true" URIEncoding="UTF-8" useBodyEncodingForURI="true" />


