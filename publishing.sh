#!/bin/bash

# ./gradlew -p zazecommon clean build uploadArchives --info
# ./gradlew -p zazeutil clean build uploadArchives --info


echo "脚本名: $0 ";
echo "参数1: $1 ";

./gradlew -p $1 clean build bintrayUpload --info