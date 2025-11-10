package com.zaze.core.designsystem.theme.ext

import android.graphics.Color
import android.os.Build
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
 * 透明状态栏，可以显示在状态后面
 */
fun AppCompatActivity.setDrawBehindSystemBars() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setStatusBarColor(Color.TRANSPARENT)
        setNavigationBar(Color.TRANSPARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        return
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        setNavigationBar(ColorUtil.darkenColor(surfaceColor()))
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setStatusBarColor(Color.TRANSPARENT)
    } else {
        setStatusBarColor(surfaceColor())
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
fun AppCompatActivity.navigationBarsVisible(visible: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        if (visible) {
            show(WindowInsetsCompat.Type.navigationBars())
        } else {
            hide(WindowInsetsCompat.Type.navigationBars())
        }
    }
}

fun AppCompatActivity.statusBarsVisible(visible: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        if (visible) {
            show(WindowInsetsCompat.Type.statusBars())
        } else {
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}

/**
 * 设置状态栏的颜色
 */
fun AppCompatActivity.setStatusBarColor(@ColorInt color: Int) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> color
        else -> ColorUtil.darkenColor(color)
    }.let {
        window.statusBarColor = it
        setLightStatusBar(it.isColorLight)
    }
}

fun AppCompatActivity.setNavigationBar(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        ColorUtil.darkenColor(color)
    } else {
        color
    }.let {
        window.navigationBarColor = it
        setLightNavigationBar(color.isColorLight)
    }
}
/**
 * [isLight] true：status设置为浅色；false 设置为 深色
 */
@Suppress("Deprecation")
fun AppCompatActivity.setLightStatusBar(isLight: Boolean) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val decorView = window.decorView
//        val systemUiVisibility = decorView.systemUiVisibility
//        if (enabled) { // 浅色
//            decorView.systemUiVisibility =
//                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        } else { // 去除 flag
//            decorView.systemUiVisibility =
//                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//        }
//    }
    WindowInsetsControllerCompat(window, window.decorView).apply {
        this.isAppearanceLightStatusBars = isLight
    }
}

fun AppCompatActivity.setLightNavigationBar(isLight: Boolean) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val decorView = window.decorView
//        var systemUiVisibility = decorView.systemUiVisibility
//        systemUiVisibility = if (isLight) {
//            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//        } else {
//            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
//        }
//        decorView.systemUiVisibility = systemUiVisibility
//    }
    WindowInsetsControllerCompat(window, window.decorView).apply {
        isAppearanceLightNavigationBars = isLight
    }
}