plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

//apply plugin: 'com.github.dcendents.android-maven'
//group = 'com.github.zaze359'

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
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
//    implementation fileTree(include: ['*.jar'], dir: 'libs')
    testImplementation(project(":core:testing"))

    api(libs.kotlin.stdlib)
    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.core.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.navigation.fragment)
    api(libs.androidx.navigation.ui.ktx)
    //
//    api("com.github.tbruyelle:rxpermissions:0.12")
    api(libs.rxjava2)
    api(libs.rxandroid)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(project(":util"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:datastore"))
}

apply(from = "${project.rootDir}/buildscripts/maven-publish.gradle")

