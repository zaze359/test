@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.zaze.core.designsystem"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
//    implementation(fileTree(baseDir = "libs"))
    api(libs.google.android.material)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

//    implementation(libs.androidx.compose.foundation)
//    implementation(libs.androidx.compose.foundation.layout)

    // splash screen
    api(libs.androidx.core.splash)

    api(libs.androidx.compose.materialWindow)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)

    api(libs.androidx.compose.ui.googlefonts)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.accompanist.permissions)
    api(libs.androidx.preference.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)


    debugApi(libs.androidx.compose.ui.tooling)
//    implementation("androidx.compose.material:material")
//    implementation("androidx.compose.foundation:foundation")
//    implementation("androidx.compose.ui:ui")

    implementation(libs.androidx.core.ktx)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
//    implementation(project(":util"))
}

apply(from = "${project.rootDir}/buildscripts/maven-publish.gradle")
