plugins {
    alias(libs.plugins.compose.compiler.plugin)
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
        compose = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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

    implementation(project(":util"))
    implementation(project(":common"))
//    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}