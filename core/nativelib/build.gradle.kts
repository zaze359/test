plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
        //        targetSdk = libs.versions.targetSdk.get().toInt()
//        versionCode rootProject.ext.versionCode
//        versionName rootProject.ext.versionName
//        externalNativeBuild {
//            cmake {
//                cppFlags ""
//            }
//        }
    }
//
//    externalNativeBuild {
//        cmake {
//            path "CMakeLists.txt"
//        }
//    }

    sourceSets {
        getByName("main") {
//            java.srcDirs = ['src/main/java']
//            assets.srcDirs = ['src/main/assets']
            jniLibs.srcDirs("libs")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(fileTree("libs"))
    testImplementation(libs.junit4)
}
