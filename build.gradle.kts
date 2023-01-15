ext {
//    set("junitVersion", "4.13.2")
    set("versionCode", 1)
    set("versionName", "1.0")
    set("kotlin_version", "1.7.20")
    set("hiltVersion", "2.44")

//    junitVersion = "4.13.2"
//    versionCode = 1
//    versionName = "1.0"
//    kotlin_version = "1.7.20"
//    hiltVersion = "2.44"
}

buildscript {

    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://localhost:8081/repository/maven-public")
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
//        classpath 'com.jakewharton:butterknife-gradle-plugin:9.0.0-rc1'
        // 上传bintray
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.0")
        // hilt
        // matrix
        classpath("com.tencent.matrix:matrix-gradle-plugin:${rootProject.extra["MATRIX_VERSION"]}") {
            isChanging = true
        }
    }
}

subprojects {
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://localhost:8081/repository/maven-public")
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
//        maven {
//            url 'file:///Users/zaze/Documents/maven/localrepo/'
//        }
//        maven {
//            url "https://dl.bintray.com/zaze359/maven"
//        }
    }
}

//apply(from = "${project.rootDir}/buildscripts/common.gradle")

//task clean (type = Delete) {
//    delete rootProject . buildDir
//}

//tasks.withType(JavaCompile) {
//    options.addStringOption('Xdoclint:none', '-quiet')
//    options.encoding = "UTF-8"
//    // 启动增量编译：Gradle 4.10 版本之后默认使用
//    options.incremental = true
//}

//tasks.withType<JavaCompile>().configureEach {
//    systemProperties.put("robolectric.logging", "stdout")
//}
