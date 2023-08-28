package com.zaze.core.designsystem.theme.ext

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.zaze.core.designsystem.util.ColorUtil

// --------------------------------------------------
fun AppCompatActivity.setImmersiveSurface(isFullScreen: Boolean) {
    if (isFullScreen) {
        setImmersiveFullscreen()
    } else {
        setDrawBehindSystemBars()
    }
}


/**
 * 设置全屏沉浸式，适配刘海屏
 */
fun AppCompatActivity.setImmersiveFullscreen() {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
        if (insets.displayCutout != null) {
            insets
        } else {
            WindowInsetsCompat.CONSUMED
        }
    }
}

/**
 * 透明状态栏，显示在状态后面
 */
fun AppCompatActivity.setDrawBehindSystemBars() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    } else {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            window.navigationBarColor = ColorUtil.darkenColor(surfaceColor())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.TRANSPARENT)
        } else {
            setStatusBarColor(Color.BLACK)
        }
    }
}


/**
 * 退出全屏
 */
fun AppCompatActivity.exitFullscreen() {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}

/**
 * 设置状态栏的颜色
 */
fun AppCompatActivity.setStatusBarColor(@ColorInt color: Int) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> window.statusBarColor = color
        else -> window.statusBarColor = ColorUtil.darkenColor(color)
    }
//    window.statusBarColor = ContextCompat.getColor(this, colorRes)
//    setLightStatusBar(color.isColorLight)
    setLightStatusBar(surfaceColor().isColorLight)
}

/**
 * [enabled] true：status设置为浅色；false 设置为 深色
 */
@Suppress("Deprecation")
fun AppCompatActivity.setLightStatusBar(enabled: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility
        if (enabled) { // 浅色
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else { // 去除 flag
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

@Suppress("Deprecation")
fun AppCompatActivity.setLightNavigationBar(enabled: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decorView = window.decorView
        var systemUiVisibility = decorView.systemUiVisibility
        systemUiVisibility = if (enabled) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        decorView.systemUiVisibility = systemUiVisibility
    }
}