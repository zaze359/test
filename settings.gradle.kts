pluginManagement {
    repositories {
        if(extra.has("useLocalMaven") && (extra["useLocalMaven"] as String).toBoolean()) {
            maven {
                isAllowInsecureProtocol = true
                url = uri("http://localhost:8081/repository/maven-public")
            }
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
//enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if(extra.has("useLocalMaven") && (extra["useLocalMaven"] as String).toBoolean()) {
            maven {
                isAllowInsecureProtocol = true
                url = uri("http://localhost:8081/repository/maven-public")
            }
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "test"
include(":app")
include(":util", ":common")


include(":feature:animations")
include(":feature:accessibility")
//include(":feature:localrepo")
include(":feature:nativelib")
include(":feature:usagestats")


include(":core:designsystem")
include(":core:testing")
include(":core:data")
include(":core:database")
include(":core:model")
include(":feature:intent")
include(":feature:communication")
include(":feature:storage")
include(":feature:notification")
include(":feature:media")
include(":core:network")
