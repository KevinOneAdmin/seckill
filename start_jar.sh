#!/usr/bin/env bash

echo '======================start seckill loding=========================='
mvn clean -Dmaven.test.skip=true package
echo '======================build seckill package ove====================='
java -server -Xms3072m -Xmx3072m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heap.dump -jar ./target/seckill.jar
