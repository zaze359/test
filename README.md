# test
ZAZE Android Base Library And Some Test Demo


## Add Base Library to your project
### Gradle:
-    compile 'com.zaze.aarrepo:aarrepo:1.2.2@aar'




# 版本更替

## 1.2.0

1. 修改Log模块工具类， 废弃LogKit.java, 替换为ZLog.java
2. 修改之前的 任务队列 机制， 废弃TaskFilterThread.java， 替换为 TaskQueueManager.java, 提供了一系列的 任务池管理,
并增加 demo  TaskActivity.java