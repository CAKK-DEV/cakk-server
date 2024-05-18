#!/usr/bin/env bash

PREFIX=/home/ubuntu
REPOSITORY=~/deploy
BUILD_DIR=/cakk-api/build/libs
PROJECT_NAME=prod-app-api
DEPLOY_PATH="$REPOSITORY/$BUILD_DIR"
DEPLOY_LOG_PATH="$PREFIX/$PROJECT_NAME/deploy.log"
DEPLOY_ERROR_LOG_PATH="$PREFIX/$PROJECT_NAME/deploy_error.log"
APPLICATION_LOG_PATH="$PREFIX/$PROJECT_NAME/application.log"
SPRING_ACTIVE_PROFILES=prod
BUILD_JAR=$(ls -tr $REPOSITORY/$BUILD_DIR/*.jar | tail -n 1)
JAR_NAME=$(basename "$BUILD_JAR")
TIME_NOW=$(date +%c)

echo "$TIME_NOW > 배포를 시작합니다." >> "$DEPLOY_LOG_PATH"

echo "$TIME_NOW > build 위치: $DEPLOY_PATH" >> $DEPLOY_LOG_PATH
echo "$TIME_NOW > build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
echo "$TIME_NOW > build 파일 복사" >> $DEPLOY_LOG_PATH
cp "$BUILD_JAR" "$DEPLOY_PATH"

DEPLOY_JAR="$DEPLOY_PATH/$JAR_NAME"

echo "$TIME_NOW > DEPLOY_JAR : $DEPLOY_JAR" >> $DEPLOY_LOG_PATH
echo "$TIME_NOW > 배포 환경 : $SPRING_ACTIVE_PROFILES" >> $DEPLOY_LOG_PATH
echo "$TIME_NOW > DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH
nohup java -jar -Dspring.profiles.active="$SPRING_ACTIVE_PROFILES" "$DEPLOY_JAR" >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERROR_LOG_PATH &

sleep 5

echo "$TIME_NOW > 배포를 종료합니다." >> $DEPLOY_LOG_PATH
echo "" >> $DEPLOY_LOG_PATH
