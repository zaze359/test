# Zaze Android Library

一个用于学习和开发的 Android 工具库集合，包含测试 Demo、工具类库和组件库。

## 📋 功能特性

### 核心模块

| 模块 | 说明 |
|:---|:---|
| **util** | 基础工具库，提供文件操作、压缩解压、字符串处理等常用工具 |
| **common** | 旧版本项目公共库，正在拆分中 |
| **core:network** | 网络访问库，封装 OkHttp，提供 HTTP 下载、请求等功能 |
| **core:database** | 数据库层，包含 DAO 和 POJO |
| **core:datastore** | DataStore 配置管理 |
| **core:designsystem** | UI 核心架构依赖，通用 View、Theme、Icon 配置 |
| **core:ui** | 业务相关的通用 UI 组件 |
| **core:model** | 面向 UI 层的数据实体类（VO） |
| **core:data** | 数据访问层，处理 VO 和 POJO 转换 |
| **core:testing** | 单元测试支持库 |
| **core:bsdiff** | APK 增量更新库，基于 bsdiff 算法实现差分更新 |

### 工具类功能

- **文件操作**：文件读写、复制、删除、目录创建
- **压缩解压**：ZIP 文件的压缩和解压
- **网络下载**：HTTP 文件下载、文本内容获取
- **日志框架**：统一日志管理（ZLog）
- **字符串处理**：字符串格式化、编码转换
- **增量更新**：APK 差分更新，基于 bsdiff 算法

## 🚀 快速开始

### 添加依赖

在项目的 `build.gradle` 中添加 Maven 仓库：

```gradle
repositories {
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://s01.oss.sonatype.org/content/groups/public") }
}
```

添加依赖：

```gradle
implementation "io.github.zaze359:zaze-utils:1.0.0"
```

### 基本使用

#### 文件操作

```kotlin
// 创建目录
FileUtil.createDirNotExists(File("/path/to/directory"))

// 写入文件
FileUtil.writeToFile(File("/path/to/file.txt"), inputStream)

// 读取文件内容
val content = FileUtil.readFromFile("/path/to/file.txt")
```

#### ZIP 压缩

```java
// 压缩文件
ZipUtil.compressFile("/path/to/source", "/path/to/output.zip")

// 解压文件
ZipUtil.unCompressToFolder("/path/to/archive.zip", "/path/to/destination")
```

#### 网络下载

```java
// 下载文件
HttpDownloader.downFile("https://example.com/file.zip", "/path/to/save.zip")

// 下载文本内容
String content = HttpDownloader.download("https://example.com/api/data");
```

## 🏗️ 项目结构

```
test/
├── app/                    # 主项目（Demo 应用）
├── util/                   # 基础工具库
│   └── src/main/java/com/zaze/utils/
│       ├── FileUtil.kt     # 文件操作工具
│       └── compress/       # 压缩解压工具
├── core/
│   ├── network/            # 网络访问库
│   ├── database/           # 数据库层
│   ├── datastore/          # DataStore 管理
│   ├── designsystem/       # UI 设计系统
│   ├── ui/                 # 通用 UI 组件
│   ├── model/              # 数据实体类
│   ├── data/               # 数据访问层
│   └── testing/            # 测试支持
└── feature/                # 功能模块（按业务拆分）
```

## 🛠️ 构建项目

### 环境要求

- Android Studio Hedgehog | 2023.1.1
- Gradle 7.6+
- Kotlin 1.8+
- Android SDK 21+

### 编译命令

```bash
# 构建项目
./gradlew build

# 构建并跳过测试
./gradlew build -x test

# 清理构建
./gradlew clean
```

## 📝 代码规范

### Java 代码规范

- 使用 `try-with-resources` 自动管理资源（InputStream、OutputStream 等）
- 使用 `ZLog` 进行日志记录，替代 `e.printStackTrace()`
- 方法参数使用 `@NonNull`、`@Nullable` 注解

### Kotlin 代码规范

- 使用 `use` 函数管理资源（替代 try-with-resources）
- 避免使用 `!!`，优先使用安全调用 `?.` 和 Elvis 运算符 `?:`
- 类和函数使用 `@JvmStatic` 注解支持 Java 调用

## 🧪 测试入口

### APK 增量更新测试

| 组件 | 路径 | 功能 |
|:---|:---|:---|
| `AppUpdateViewModel` | [app/src/main/java/com/zaze/demo/update/AppUpdateViewModel.kt](file:///Users/zhaozhen/Documents/GitRepository/test/app/src/main/java/com/zaze/demo/update/AppUpdateViewModel.kt) | 测试逻辑实现 |
| `AppUpdateScreen` | [app/src/main/java/com/zaze/demo/update/AppUpdateScreen.kt](file:///Users/zhaozhen/Documents/GitRepository/test/app/src/main/java/com/zaze/demo/update/AppUpdateScreen.kt) | 测试界面 |

**测试功能：**
- **卸载应用**：卸载指定包名的应用
- **安装旧版本**：安装测试用的旧版本 APK
- **应用增量包**：下载补丁并应用到当前安装的 APK，生成新 APK 并安装

**测试准备：**

在设备的 `/sdcard/zaze/bsdiff/` 目录下放置以下文件：
- `old.apk` - 旧版本 APK（用于生成补丁或作为基准版本）
- `new.apk` - 新版本 APK（用于生成补丁）
- `patch.apk` - 补丁文件（用于应用补丁测试）

**工作流程：**

```
┌─────────────────────────────────────────────────────────────┐
│                    增量更新流程                             │
├─────────────────────────────────────────────────────────────┤
│ 【服务端】                                                  │
│   old.apk + new.apk → bsdiff → patch.apk (上传到服务器)     │
├─────────────────────────────────────────────────────────────┤
│ 【客户端】                                                  │
│   1. 下载 patch.apk                                        │
│   2. 获取当前安装的 old.apk                                 │
│   3. applyPatch(old.apk, new.apk, patch.apk)              │
│   4. 安装生成的 new.apk                                    │
└─────────────────────────────────────────────────────────────┘
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 提交代码前

1. 确保代码通过编译：`./gradlew build -x test`
2. 运行测试：`./gradlew test`
3. 检查代码格式：`./gradlew spotlessCheck`

## 📄 许可证

```
Copyright 2024 ZAZE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 📧 联系

- 作者：ZAZE
- GitHub：[https://github.com/zaze359](https://github.com/zaze359)
