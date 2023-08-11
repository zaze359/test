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
                cppFlags += "-std=c++11"
            }
            ndk {
                // 'x86', 'x86_64', 'armeabi', 'armeabi-v7a', 'arm64-v8a'
                abiFilters.addAll(arrayOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a"))
            }
        }
    }

    ndkVersion = "16.1.4479499"

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }


//    sourceSets {
//        getByName("main") {
////            java.srcDirs = ['src/main/java']
////            assets.srcDirs = ['src/main/assets']
//            jniLibs.srcDirs("libs")
//        }
//    }


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // 打包
//    packagingOptions {
//        jniLibs.pickFirsts.add("**/jpeg.so")
//        jniLibs.pickFirsts.add("**/jpeg-turbo.so")
//    }
}

dependencies {
    implementation(fileTree("libs"))
    testImplementation(libs.junit4)
//    testImplementation(project(":core:testing"))

    implementation(project(":common"))
    implementation(project(":util"))
}
