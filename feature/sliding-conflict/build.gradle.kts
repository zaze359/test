plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zaze.feature.sliding.conflict"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        enable = true
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
//    testImplementation(libs.junit4)
//    androidTestImplementation(libs.androidx.test.core)
//    androidTestImplementation(libs.androidx.test.espresso.core)
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
//    implementation(libs.androidx.recyclerview)

    implementation(libs.zaze.util)
    implementation(libs.zaze.common)
//    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}