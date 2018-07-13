#!/usr/bin/env bash

echo '============================debug start seckill ========================='
mvn clean -Dmaven.test.skip=true package
echo '============================build seckill over==========================='
echo '=============================start  debug port 5005======================'
java -server -Xms3072m -Xmx3072m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heap.dump -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n -jar ./target/seckill.jar
