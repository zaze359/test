plugins {
    alias(libs.plugins.compose.compiler.plugin)
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zaze.demo.feature.anim"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)

    implementation(project(":util"))
    implementation(project(":common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))


}
