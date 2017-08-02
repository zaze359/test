# test

ZAZE Android Base Library And Some Test Demo


## Add Base Library to your project

### Gradle:

-    compile 'com.zaze.aarrepo:aarrepo:1.4.19@aar'


# 版本更替说明

## 1.2.x

1. 修改Log模块工具类， 废弃LogKit.java, 替换为ZLog.java
2. 修改之前的 任务队列 机制， 废弃TaskFilterThread.java， 替换为 TaskQueueManager.java, 提供了一系列的 任务池管理,
并增加 demo  TaskActivity.java

## 1.3.x
1.补充优化工具类
2.采用包装模式 重构了 任务池
3.优化 Task 机制，过滤执行中任务的重复添加（对于外部异步线程调用效果不大）

## 1.4.x
1.补充优化工具类
2.重写了ECallback
3.学习kotlin
4.Task支持添加任务 addFirst
