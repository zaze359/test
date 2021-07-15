package com.zaze.common.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:35
 */
object ScreenUtils {
    // --------------------------------------------------
    fun addLayoutFullScreen(window: Window, isAllFull: Boolean = false) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or getLayoutFullScreenFlag(
                isAllFull
            )
    }

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

    /**
     * 获得屏幕宽度
     *
     * @param context context
     * @return 屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }
}