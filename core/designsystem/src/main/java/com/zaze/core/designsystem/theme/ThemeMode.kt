package com.zaze.core.designsystem.theme

import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode {
    /** 浅色 **/
    LIGHT,
    /** 深色 **/
    DARK,
    /** 跟随系统 **/
    AUTO
}

fun String?.toThemeMode(): ThemeMode = when (this) {
    "light" -> ThemeMode.LIGHT
    "dark" -> ThemeMode.DARK
    else -> ThemeMode.AUTO
}

fun ThemeMode?.toNightMode(): Int = when (this) {
    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
}

