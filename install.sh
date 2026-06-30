#!/bin/bash
./gradlew clean assembleDebug --info
adb install ./app/build/outputs/apk/debug/app-debug.apk
adb shell am start com.zaze.demo/.SplashActivity