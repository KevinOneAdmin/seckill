#!/usr/bin/env bash

echo "脚本无法直接运行!"
exit 1
#上传文件
scp ~/nginx-1.15.1.tar.gz kevin@192.168.56.101:/home/work
#缓存清理
scp ~/ngx_cache_purge-2.3.tar.gz kevin@192.168.56.101:/home/work
#正则表达式
scp ~/pcre-8.42.zip kevin@192.168.56.101:/home/work
#文件解压
tar -zxvf nginx-1.15.1.tar.gz
tar -zxvf ngx_cache_purge-2.3.tar.gz
unzip pcre-8.42.zip

#如果没有安装gcc g++要先安装这些
cd ./nginx-1.15.1
su root
./configure --prefix=/usr/local/nginx --with-pcre=/home/work/pcre-8.42 --with-http_stub_status_module --with-http_gzip_static_module --add-module=/home/work/ngx_cache_purge-2.3


#Nginx-top
yum install epel-release
yum install python-pip

pip install ngxtop

cd /usr/local/nginx
#指定配置文件
ngxtop -c ./conf/nginx.conf
#查询状态是200的
ngxtop -c ./conf/nginx.conf --filter 'status == 200'
#查询ip访问最多的
ngxtop -c ./conf/nginx.conf  --group-by remote_addr
