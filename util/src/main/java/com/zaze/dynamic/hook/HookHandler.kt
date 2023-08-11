package com.zaze.dynamic.hook

import android.os.Handler

object HookHandler {
    // 获取 Handler中的 mCallback 字段
    val mCallbackField by lazy {
        Handler::class.java.getDeclaredField("mCallback").apply { isAccessible = true }
    }
    /**
     * 替换 Handler.mCallback，并将原始 mCallback 返回出去
     */
    fun swapCallback(handler: Handler, callback: (Handler.Callback?) -> Handler.Callback?) {
        // 修改属性值
        mCallbackField[handler] = callback(mCallbackField[handler] as Handler.Callback?)
    }

}