#!/bin/bash

echo "脚本名: $0 ";
echo "库名: $1 ";

#./gradlew -p $1 clean build bintrayUpload --info
#./gradlew -p $1 uploadArchives --info

# 上传到 MavenLocal
./gradlew :$1:publishReleasePublicationToMavenLocal --info
# 上传到远程maven仓库
./gradlew :$1:publishReleasePublicationToMavenRepository --info

#./gradlew :util:publishReleasePublicationToMavenLocal --info
#./gradlew :util:publishReleasePublicationToMavenRepository --info

#./gradlew :core:designsystem:publishReleasePublicationToMavenLocal --info
#./gradlew :core:designsystem:publishReleasePublicationToMavenRepository --info