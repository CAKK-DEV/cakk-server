#!/usr/bin/env bash

PREFIX=/home/ubuntu
PROJECT_NAME=prod-app-api
REPOSITORY="$PREFIX/deploy"
BUILD_DIR=cakk-api/build/libs
DEPLOY_LOG_PATH="$PREFIX/$PROJECT_NAME/deploy.log"
BUILD_JAR=$(ls -tr $REPOSITORY/$BUILD_DIR/*.jar | tail -n 1)
JAR_NAME=$(basename "$BUILD_JAR")

TIME_NOW=$(date +%c)

chmod +x "$DEPLOY_LOG_PATH"
echo "$TIME_NOW> 현재 실행 중인 서버 pid 확인" >> DEPLOY_LOG_PATH
CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -z "$CURRENT_PID" ]
then
  echo "$TIME_NOW > 현재 구동중인 서버가 없습니다." >> DEPLOY_LOG_PATH
else
  echo "$TIME_NOW > 현재 구동중인 서버를 종료시키겠습니다." >> DEPLOY_LOG_PATH
  echo "$TIME_NOW > kill -9 $CURRENT_PID" >> DEPLOY_LOG_PATH
  kill -9 "$CURRENT_PID"
  sleep 5
fi
