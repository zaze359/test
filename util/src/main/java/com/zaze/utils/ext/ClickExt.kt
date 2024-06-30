package com.zaze.utils.ext

import android.os.SystemClock
import android.view.View

private val TAG_LAST_CLICK_TIME by lazy {
    "z_last_click_time".hashCode()
}
private const val DEFAULT_DELAY = 500L


/**
 * 扩展属性：表示最近点击时间，通过tag 来保存
 */
private var <T : View> T.lastClickTime: Long
    get() = if (getTag(TAG_LAST_CLICK_TIME) != null) getTag(TAG_LAST_CLICK_TIME) as Long else -1
    set(value) {
        setTag(TAG_LAST_CLICK_TIME, value)
    }

fun <T : View> T.setSingleClickListener(delay: Long = DEFAULT_DELAY, block: (T) -> Unit) {
    setOnClickListener {
        if (clickEnable(delay)) {
            it.isEnabled = false
            it.postDelayed(Runnable {
                it.isEnabled = true
            }, DEFAULT_DELAY)
            block(it as T)
        }
    }
}

private fun <T : View> T.clickEnable(clickDelay: Long): Boolean {
    val currentClickTime = SystemClock.uptimeMillis()
    return if (currentClickTime - lastClickTime >= clickDelay) {
        lastClickTime = currentClickTime
        true
    } else {
        false
    }
}