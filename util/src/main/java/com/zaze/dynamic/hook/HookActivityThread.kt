package com.zaze.dynamic.hook

import android.os.Handler

object HookActivityThread {

    // 通过类名获取 class
    private val activityThreadClass by lazy { Class.forName("android.app.ActivityThread") }

    // 通过静态方法 currentActivityThread() 获取 activityThread 实例
    val activityThreadInstance by lazy {
        activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)
    }

    /**
     * 替换 ActivityThread中的Handler.mCallback，并将原始 mCallback 返回出去
     */
    fun getHandler():Handler {
        // 获取 ActivityThread 中的 mH 字段信息
        val mHField =
            activityThreadClass.getDeclaredField("mH").apply { isAccessible = true }
        // 获取 ActivityThread 中的 Handler 实例
        return mHField[activityThreadInstance] as Handler
    }

    /**
     * 替换 ActivityThread中的Handler.mCallback，并将原始 mCallback 返回出去
     */
    fun swapHandlerCallback(swap: (Handler.Callback?) -> Handler.Callback?) {
        HookHandler.swapCallback(getHandler(), swap)
    }

}