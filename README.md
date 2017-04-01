# test
ZAZE Android Base Library And Some Test Demo


## Add Base Library to your project
### Gradle:
-    compile 'com.zaze.aarrepo:aarrepo:1.3.7@aar'




# 版本更替

## 1.2.x

1. 修改Log模块工具类， 废弃LogKit.java, 替换为ZLog.java
2. 修改之前的 任务队列 机制， 废弃TaskFilterThread.java， 替换为 TaskQueueManager.java, 提供了一系列的 任务池管理,
并增加 demo  TaskActivity.java


## 1.3.x
1.采用包装模式 重构了 任务池
2.补充丰富工具类