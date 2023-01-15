plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
//    id("com.tencent.matrix-plugin")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.zaze.demo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = rootProject.ext["versionCode"] as Int
        versionName = rootProject.ext["versionName"] as String

//        testInstrumentationRunnerArguments clearPackageData: 'true'
//        multiDexEnabled = true
//        javaCompileOptions { annotationProcessorOptions { includeCompileClasspath = true } }
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    // region compose
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    // endregion compose


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
        named("debug") {
            storeFile = file("android_zaze.keystore")
            storePassword = "3184582"
            keyAlias = "android"
            keyPassword = "3184582"
        }
        create("release") {
            storeFile = file("android_zaze.keystore")
            storePassword = "3184582"
            keyAlias = "android"
            keyPassword = "3184582"
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

//    kapt {
//        correctErrorTypes = true
//    }

//    lint.abortOnError = false

    dataBinding {
        enable = true
    }
    configurations.all {
//        resolutionStrategy.force 'com.google.code.findbugs:jsr305:1.3.9'
    }
}

dependencies {

    implementation(fileTree(baseDir = "libs"))
    testImplementation(libs.junit)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.rxjava2)
    implementation(libs.okhttp3)
    implementation(libs.rxandroid)
    implementation(libs.eventbus)
    implementation(libs.androidx.constraintlayout)

    // region hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    // endregion hilt

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)


    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.materialWindow)

    // Choose one of the following:
    implementation(libs.androidx.compose.material3)
//    implementation(libs.google.android.material)
//    implementation("androidx.compose.material:material")
//    implementation("androidx.compose.foundation:foundation")
//    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // UI Tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

//    // Optional - Included automatically by material, only add when you need
//    // the icons but not the material library (e.g. when using Material3 or a
//    // custom design system based on Foundation)
//    implementation("androidx.compose.material:material-icons-core")
//    // Optional - Add full set of material icons
//    implementation("androidx.compose.material:material-icons-extended")
//    // Optional - Add window size utils
//    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.runtime.livedata)
    // Optional - Integration with RxJava
//    implementation("androidx.compose.runtime:runtime-rxjava2")
    implementation(libs.androidx.compose.ui.googlefonts)


    implementation(project(":util"))
    implementation(project(":common"))
    implementation(project(":module_animations"))
//    implementation project(':module_accessibility')
//    implementation(project(':module_usagestats'))
    implementation(project(":module_usagestats"))







//    implementation("androidx.multidex:multidex:2.0.1")
//    testImplementation 'org.hamcrest:hamcrest-all:1.3'
//    testImplementation 'org.mockito:mockito-all:1.10.19'
//    androidTestImplementation 'org.mockito:mockito-core:2.23.4'
//    androidTestImplementation 'com.google.dexmaker:dexmaker:1.2'
//    androidTestImplementation 'com.google.dexmaker:dexmaker-mockito:1.2'
    //


//    implementation 'com.tencent.bugly:crashreport:2.8.6.0'
//    implementation 'com.jaredrummler:android-processes:1.1.1'

    //
//    api 'com.blankj:utilcodex:1.25.8'
    // wechat-sdk
    //    implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'

    // ---------------------
    // region matrix
    val MATRIX_VERSION: String by rootProject.extra
    println("matrixVersion: ${MATRIX_VERSION}")
    implementation(
        group = "com.tencent.matrix",
        name = "matrix-android-lib",
        version = MATRIX_VERSION,
    )
    implementation(
        group = "com.tencent.matrix",
        name = "matrix-trace-canary",
        version = MATRIX_VERSION,
    )
    implementation(
        group = "com.tencent.matrix",
        name = "matrix-io-canary",
        version = MATRIX_VERSION,
    )
    // endregion matrix
    // ---------------------

}

//apply(from = "${project.rootDir}/buildscripts/test.gradle")


//matrix {
//    trace {
//        enable = true    //if you don't want to use trace canary, set false
//        baseMethodMapFile = "${project.buildDir}/matrix_output/Debug.methodmap"
//        blackListFile = "${project.projectDir}/matrixTrace/blackMethodList.txt"
//    }
//}