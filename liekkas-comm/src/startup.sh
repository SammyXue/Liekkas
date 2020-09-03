#!/bin/bash
APP_NAME=liekkas-1.0-SNAPSHOT.jar

start(){
    is_exist
    if [ $? -eq "0" ]; then
        echo "${APP_NAME} running. pid=${pid}"
    else
        nohup java -Dfile.encoding=UTF-8 -jar $APP_NAME > log.file 2>log.error &
        echo "${APP_NAME} started"
    fi
}

is_exist(){
    pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
    if [ -z "${pid}" ]; then
        return 1
    else
        return 0
    fi
}
echo "start..."
start
echo "start finish..."


