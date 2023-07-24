#!/bin/bash


echo "脚本名: $0 ";
echo "参数1: $1 ";

#./gradlew -p $1 clean build bintrayUpload --info
#./gradlew -p $1 uploadArchives --info
# 上传到远程maven仓库
#./gradlew :$1:publishAllPublicationsToMavenRepository --info
# 上传到 MavenLocal
./gradlew :util:publishDebugPublicationToMavenLocal --info
