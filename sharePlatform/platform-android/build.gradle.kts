plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.zaze.platform.android"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
}

dependencies {
    implementation(project(":shareLogic:logic-core"))
    implementation(project(":shareLogic:logic-domain"))
    implementation(project(":shareLogic:logic-data"))
}
