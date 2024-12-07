package com.zaze.core.designsystem.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.use
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.zaze.core.designsystem.theme.ThemeMode
import com.zaze.core.designsystem.theme.widgets.Tintable
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-20 - 18:37
 */
object ThemeUtils {

    var sRecycler: Field? = null
    private var sRecycleViewClearMethod: Method? = null


    fun refreshUI(context: Context) {
//        val rootView = getWrapperActivity(context)?.findViewById<View>(android.R.id.content)
        val rootView = getWrapperActivity(context)?.findViewById<View>(Window.ID_ANDROID_CONTENT)
        refreshView(rootView)
    }

    private fun refreshView(view: View?) {
        if (view is Tintable) {
            view.tint()
        }
        when (view) {
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    refreshView(view.getChildAt(i))
                }
            }

            else -> {
            }
        }
    }

    // --------------------------------------------------
    private fun getWrapperActivity(context: Context?): Activity? {
        return when (context) {
            is Activity -> {
                context
            }

            is ContextWrapper -> {
                getWrapperActivity(
                    context.baseContext
                )
            }

            else -> {
                null
            }
        }
    }
    // --------------------------------------------------

    fun Context.getThemeMode(modeName: String): ThemeMode = when (modeName) {
        "LIGHT" -> ThemeMode.LIGHT
        "DARK" -> ThemeMode.DARK
        else -> ThemeMode.AUTO
    }

    @StyleRes
    fun Context.getNightMode(mode: ThemeMode): Int = when (mode) {
        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

//    fun isWindowBackgroundDark(context: Context): Boolean {
//        return !ColorUtil.isColorLight(resolveColor(co ntext, R.attr.windowBackground))
//    }

    @JvmOverloads
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int = 0): Int {
        context.theme.obtainStyledAttributes(intArrayOf(attr)).use {
            return try {
                it.getColor(0, fallback);
            } catch (e: java.lang.Exception) {
                Color.BLACK
            }
        }
    }

    @Suppress("DEPRECATION")
    fun setLayoutFullScreen(window: Window, isAllFull: Boolean = false): Int {
        val systemUiVisibility =
            window.decorView.systemUiVisibility or getLayoutFullScreenFlag(isAllFull)
        window.decorView.systemUiVisibility = systemUiVisibility
        return systemUiVisibility
    }

    @Suppress("DEPRECATION")
    fun getLayoutFullScreenFlag(isAllFull: Boolean = false): Int {
        var flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (isAllFull) {
            flag = flag or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        return flag
    }
}