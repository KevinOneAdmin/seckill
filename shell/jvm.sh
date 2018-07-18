#!/usr/bin/env bash

JAVA_OPTS -server -Xms3072m -Xmx3072m -XX:metaspaceSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heap.dump