#!/bin/bash

SERVICE_NAME=PUBLISAIZ
PATH_TO_JAR=backend/target/publisaiz-0.0.1-SNAPSHOT.jar
PID_PATH_NAME_JAVA=/tmp/PUBLISAIZ_java-pid
PID_PATH_NAME_ANGULAR=/tmp/PUBLISAIZ_angular-pid
PID_PATH_NAME=/tmp/PUBLISAIZ-pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -Dspring.profiles.active=dev -jar $PATH_TO_JAR >> backend_PUBLISAIZ.log 2>&1&
                        echo $! > $PID_PATH_NAME_JAVA
            cd frontend/angular
            ng serve -o >> ../../frontend_PUBLISAIZ.log 2>&1&
                        echo $! > $PID_PATH_NAME_ANGULAR
                        NG_PID=$(ps -ef | grep -m1 'ng serve' | awk '{print $2}');
                        PID=$(cat $PID_PATH_NAME_JAVA);
            echo "$SERVICE_NAME started PID: $PID AND $NG_PID"
        else
            PID=$(cat $PID_PATH_NAME_JAVA);
            echo "$SERVICE_NAME is already running with PID: $PID"
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME_JAVA ]; then
            PID=$(cat $PID_PATH_NAME_JAVA);
            echo "$SERVICE_NAME java stoping PID:  $PID"
            kill $PID;
            echo "$SERVICE_NAME java stopped PID:  $PID"
            rm $PID_PATH_NAME_JAVA
            NG_PID=$(ps -ef | grep -m1 'ng serve' | awk '{print $2}');
            echo "$SERVICE_NAME angular stoping PID:  $NG_PID"
            kill -9 $NG_PID;
            echo "$SERVICE_NAME angular stopped PID:  $NG_PID"
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac
