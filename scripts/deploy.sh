#!/bin/sh

REPOSITORY=/home/ubuntu/app
echo "👀   [$REPOSITORY] 경로로 이동합니다."
cd $REPOSITORY

APP_NAME=sidepeek
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "👀   실행 중인 애플리케이션이 없어 곧바로 실행합니다."
else
  echo "❌    실행 중인 애플리케이션이 있어 이를 종료합니다. [PID = $CURRENT_PID]"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "👀   $JAR_PATH 배포합니다!"
nohup java -jar build/libs/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
