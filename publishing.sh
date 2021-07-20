#!/bin/bash


echo "脚本名: $0 ";
echo "参数1: $1 ";

#./gradlew -p $1 clean build bintrayUpload --info
./gradlew -p $1 uploadArchives --info
#./gradlew -p util clean build uploadArchives --info