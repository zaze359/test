buildscript {

    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.gradle.plugin)
//        classpath 'com.jakewharton:butterknife-gradle-plugin:9.0.0-rc1'
        // 上传bintray
//        classpath(libs.gradle.bintray.plugin)
//        classpath(libs.android.maven.gradle.plugin)

//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
//        classpath("com.alibaba:arouter-register:1.0.2")
        // matrix
        classpath("com.tencent.matrix:matrix-gradle-plugin:${rootProject.extra["MATRIX_VERSION"]}") {
            isChanging = true
        }
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
//    id("com.android.application:7.4.2") apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
//    alias(libs.plugins.gradle.versions)
//    alias(libs.plugins.version.catalog.update)
}
println("project build : -----------------------------------------------------")

//apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
//apply(from = "${project.rootDir}/buildscripts/common.gradle")

//task clean (type = Delete) {
//    delete rootProject . buildDir
//}

//tasks.withType(JavaCompile) {
//    options.addStringOption('Xdoclint:none', '-quiet')
//    options.encoding = "UTF-8"
//    // 启动增量编译：Gradle 4.10 版本之后默认使用
//    options.incremental = true
//}

//tasks.withType<JavaCompile>().configureEach {
//    systemProperties.put("robolectric.logging", "stdout")
//}
