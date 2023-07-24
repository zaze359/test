plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
//    id("com.android.library")
//    id("kotlin-android")
//    id("kotlin-kapt")
}

//apply plugin: 'com.github.dcendents.android-maven'
//group = 'com.github.zaze359'

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
//        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
//    buildTypes {
//        release {
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
}

dependencies {
//    implementation fileTree(include: ['*.jar'], dir: 'libs')
//    val composeBom = platform(libs.androidx.compose.bom)
//    implementation(composeBom)
//    androidTestImplementation(composeBom)
    testImplementation(libs.junit4)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    //
    api(libs.rxjava2)
    api("com.github.tbruyelle:rxpermissions:0.12")
    api(libs.rxandroid)

//    implementation(libs.okhttp3)
//    implementation(libs.okhttp.logging)

    implementation(libs.zaze.util)
//    compileOnly(project(":util"))
}

apply(from = "${project.rootDir}/buildscripts/maven-publish.gradle")

