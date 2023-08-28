# 主要内容

包含 学习用的测试demo、工具类库、组件库等

## gradle
```
maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
maven { url = uri("https://s01.oss.sonatype.org/content/groups/public") }
maven { url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2") }
```
```
implementation "io.github.zaze359:zaze-utils:1.0.0"
```
## 项目结构说明
项目整体架构参考 Google 的 [nowinandroid](https://github.com/android/nowinandroid/)

|模块|说明|
|:--|:--|
|app|主项目|
|util|基础工具库|
|common|旧版本项目公共库，拆分中|
|core:model|面向UI层的数据实体类，即VO|
|core:data|数据访问层，包含处理VO和POJO间的转换，提供Repository访问多种数据源数据等|
|core:database|数据库层，包含 DAO，POJO|
|core:datastore|管理datastore，提供配置信息相关的数据|
|core:designsystem|UI核心架构依赖，通用View，Theme、Icon配置等|
|core:ui|和业务相关的通用UI|
|core:testing|用于单元测试，主要是提供依赖，以及一些便于测试功能函数。|
|core:network|网络访问库，封装okhttp，提供网络访问相关功能|
|feature:xxx|各种功能模块|

