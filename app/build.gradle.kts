plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
//    id("com.tencent.matrix-plugin")
}

android {
    namespace = "com.zaze.demo"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = namespace
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunnerArguments clearPackageData: 'true'
//        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    dataBinding {
        enable = true
    }

    testOptions {
//        execution 'ANDROID_TEST_ORCHESTRATOR'
        unitTests {
            isReturnDefaultValues = true
            // 测试依赖于资源时开启，Android Studio 3.4 及更高版本默认提供编译版本的资源
            isIncludeAndroidResources = true
        }
    }


//    splits {
//        abi {
//            enable true
//            reset()
//            include 'armeabi', 'x86', 'armeabi-v7a', 'x86_64'
//            universalApk true
//        }
//    }

//    packagingOptions {
//        exclude("LICENSE.txt")
//        exclude("META-INF/DEPENDENCIES")
//        exclude("META-INF/NOTICE")
//        exclude("META-INF/LICENSE")
//        exclude("META-INF/LICENSE.txt")
//        exclude("META-INF/NOTICE.txt")
//    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("android_demo.keystore")
            storePassword = "123456"
            keyAlias = "android"
            keyPassword = "123456"
        }
        create("release") {
            storeFile = file("android_demo.keystore")
            storePassword = "123456"
            keyAlias = "android"
            keyPassword = "123456"
        }
    }

    sourceSets {
        getByName("main") {
            jni.srcDirs("libs")
        }
//        debug {
//        }
//        release {
//        }
//        main.java.srcDirs += 'src/main/kotlin'
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = true
//            zipAlignEnabled true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    kapt {
        correctErrorTypes = true
//        includeCompileClasspath = true

        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }
//    lint.abortOnError = false

}

configurations.all {
    // check for updates every build
//    resolutionStrategy.cacheChangingModulesFor(1, "seconds")
//    resolutionStrategy.cacheDynamicVersionsFor(1, "seconds")

//        resolutionStrategy.force 'com.google.code.findbugs:jsr305:1.3.9'
}

dependencies {
    implementation(fileTree(baseDir = "libs"))

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.coil.kt.compose)

    implementation(libs.androidx.tracing.ktx)

    implementation(libs.google.gson)
//    implementation(libs.rxjava2)
//    implementation(libs.rxandroid)
    implementation(libs.eventbus)

    //    implementation(libs.okhttp3)


    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    implementation(libs.kotlinx.coroutines.android)


    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.compose.runtime.livedata)
//    implementation("androidx.compose.runtime:runtime-rxjava2")

    //
    debugImplementation(libs.leakcanary.debug)

//    implementation("androidx.multidex:multidex:2.0.1")
//    testImplementation 'org.hamcrest:hamcrest-all:1.3'
//    testImplementation 'org.mockito:mockito-all:1.10.19'
//    androidTestImplementation 'org.mockito:mockito-core:2.23.4'
//    androidTestImplementation 'com.google.dexmaker:dexmaker:1.2'
//    androidTestImplementation 'com.google.dexmaker:dexmaker-mockito:1.2'

//    implementation 'com.tencent.bugly:crashreport:2.8.6.0'
//    implementation 'com.jaredrummler:android-processes:1.1.1'

    //
//    api 'com.blankj:utilcodex:1.25.8'
    // wechat-sdk
    //    implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'

    // --------------------------

    implementation(libs.arouter.api)
    kapt(libs.arouter.compiler)


    // ---------------------
    // region matrix
//    val MATRIX_VERSION: String by rootProject.extra
//    println("matrixVersion: ${MATRIX_VERSION}")
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-android-lib",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-android-commons",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-trace-canary",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-resource-canary-android",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-resource-canary-common",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-io-canary",
//        version = MATRIX_VERSION,
//    ){
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-sqlite-lint-android-sdk",
//        version = MATRIX_VERSION,
//    ) {
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-battery-canary",
//        version = MATRIX_VERSION,
//    ) {
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-backtrace",
//        version = MATRIX_VERSION,
//    ) {
//        isChanging = true
//    }
//    implementation(
//        group = "com.tencent.matrix",
//        name = "matrix-hooks",
//        version = MATRIX_VERSION,
//    ) {
//        isChanging = true
//    }

    // endregion matrix
    // ---------------------

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

//    implementation(libs.zaze.util)
    implementation(project(":util"))
//    implementation(libs.zaze.common)
    implementation(project(":common"))

    implementation(project(":feature:animations"))
    implementation(project(":feature:usagestats"))
    implementation(project(":feature:intent"))
    implementation(project(":feature:communication"))
    implementation(project(":feature:storage"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:media"))
    implementation(project(":feature:image"))
    implementation(project(":feature:sliding-conflict"))
    implementation(project(":feature:applications"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:accessibility"))


    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
//    implementation(project(":core:nativelib"))
//    implementation(project(":core:bsdiff"))

    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

}

//apply(from = "${project.rootDir}/buildscripts/test.gradle")
//apply(from = "${project.rootDir}/buildscripts/matrix.gradle")

//matrix {
//    logLevel = "D"
//    trace {
//        isEnable = true    // if you don't want to use trace canary, set false
//        baseMethodMapFile = "${project.buildDir}/matrix_output/Debug.methodmap"
//        blackListFile = "${project.projectDir}/matrixTrace/blackMethodList.txt"
//    }
//}