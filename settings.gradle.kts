pluginManagement {
//    includeBuild("../build-logic")

    val localPropertiesFile = File(rootProject.projectDir, "local.properties")
    val properties = java.util.Properties()
    properties.load(java.io.DataInputStream(localPropertiesFile.inputStream()))
    extra["useLocalMaven"] = properties.getProperty("useLocalMaven", "false").toBoolean()
    //
    repositories {
//        if(extra.has("useLocalMaven") && (extra["useLocalMaven"] as String).toBoolean()) {
        if (extra["useLocalMaven"] == true) {
//            println("extra22: ${extra["useLocalMaven"]}")
            maven {
                isAllowInsecureProtocol = true
                url = uri("http://localhost:8081/repository/maven-public")
            }
        }
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
//enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (extra["useLocalMaven"] == true) {
            maven {
                isAllowInsecureProtocol = true
                url = uri("http://localhost:8081/repository/maven-public")
            }
        }
        mavenLocal()
//        maven { url = uri("file:${rootDir.path}/lib") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }

//    // 指定自定义的 versionCatalogs
//    versionCatalogs {
//        create("aa") {
//            from(files("gradle/aa.versions.toml"))
//        }
//    }
}

rootProject.name = "test"
include(":app")
include(":util", ":common")

include(":feature:animations")
include(":feature:accessibility")
//include(":feature:localrepo")
include(":feature:usagestats")

//include(":core:arouter-api")
//include(":core:bsdiff")
//include(":core:nativelib")
include(":core:designsystem")
include(":core:testing")
include(":core:data")
include(":core:database")
include(":core:model")
include(":core:network")

include(":feature:intent")
include(":feature:communication")
include(":feature:storage")
include(":feature:notification")
include(":feature:media")
include(":feature:image")
include(":feature:sliding-conflict")
include(":feature:applications")
//include(":feature:duplicatedbitmap")
include(":core:datastore")
include(":feature:settings")
include(":core:ui")
