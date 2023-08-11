plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zaze.demo.core.bsdiff"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
//                cppFlags("")
                targets.addAll(arrayOf("bsdiff", "bsdiff-android"))
//                targets.addAll(arrayOf("bsdiff"))
//                targets.addAll(arrayOf("bsdiff-android"))
//                cppFlags += "-std=c++11"
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
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation(libs.junit4)
//    implementation("androidx.core:core-ktx:1.8.0")
//    implementation("androidx.appcompat:appcompat:1.4.1")
//    implementation("com.google.android.material:material:1.5.0")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.3")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}