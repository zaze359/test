package com.zaze.demo.component.floating

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zaze.demo.util.SingletonBuilder

/**
 * 悬浮窗管理类
 */
class FloatingManager(context: Context) {
    companion object : SingletonBuilder<FloatingManager, Context>() {
        override fun create(params: Context): FloatingManager {
            return FloatingManager(params)
        }

    }

    private val mWindowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * 添加悬浮窗
     * @param view  控件
     * @param params  添加到windowManager需要的layout参数
     */
    fun addView(view: View, params: WindowManager.LayoutParams? = null) {
        mWindowManager.addView(view, params)
    }

    /**
     * 移除悬浮窗
     * @param view 控件
     */
    fun removeView(view: View) {
        mWindowManager.removeView(view)
    }

    /**
     * 更新悬浮窗参数
     * @param view  控件
     * @param params  参数
     * @return
     */
    fun updateView(view: View, params: WindowManager.LayoutParams?) {
        mWindowManager.updateViewLayout(view, params)
    }

}