import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncludeCompileClasspath

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
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

    kapt {
        correctErrorTypes = true
//        includeCompileClasspath = true

        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }

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
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)

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

    implementation(libs.androidx.compose.materialWindow)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.compose.runtime.livedata)
//    implementation("androidx.compose.runtime:runtime-rxjava2")

    implementation(libs.zaze.util)
    implementation(libs.zaze.common)
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    implementation(project(":feature:animations"))
    implementation(project(":feature:usagestats"))
    implementation(project(":feature:intent"))
    implementation(project(":feature:communication"))
    implementation(project(":feature:storage"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:media"))
    implementation(project(":feature:drawable"))
    implementation(project(":feature:sliding-conflict"))
//    implementation project(':module_accessibility')
//    implementation(project(':module_usagestats'))


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