@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

//apply plugin: 'com.github.dcendents.android-maven'
//group = 'com.github.zaze359'

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
//        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
//    val deps: LinkedHashMap<Any, Any> by rootProject.ext
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext)
//    val atsl: LinkedHashMap<Any, Any> by deps
//    val espresso: LinkedHashMap<Any, Any> by deps
//    androidTestImplementation("${espresso["core"]}")

//    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.ext["kotlin_version"]}")
//    val coroutines: LinkedHashMap<Any, Any> by deps
//    implementation("${coroutines["android"]}")
//    implementation("${deps["annotations"]}")

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.gson)

    implementation(libs.rxjava2)
    implementation(libs.rxandroid)
}

apply(from = "${project.rootDir}/buildscripts/maven-publish.gradle")
