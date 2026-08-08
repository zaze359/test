plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler.plugin)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":shareLogic:logic-core"))
                implementation(project(":shareLogic:logic-domain"))
                implementation(compose.runtime)
            }
        }
    }
}
