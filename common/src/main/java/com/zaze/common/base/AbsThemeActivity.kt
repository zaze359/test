package com.zaze.common.base

import android.os.Bundle
import com.zaze.common.R
import com.zaze.common.base.ext.setImmersion
import com.zaze.common.base.ext.setImmersionOnWindowFocusChanged

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:27
 */
abstract class AbsThemeActivity : AbsLogActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImmersion(isFullScreen(), getStatusBarColor())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setImmersionOnWindowFocusChanged(isFullScreen(), hasFocus)
    }

    /**
     * 获取状态栏色值
     * color res id
     */
    protected open fun getStatusBarColor(): Int {
        return R.color.colorPrimary
    }

    /**
     * 是否全屏
     */
    protected open fun isFullScreen(): Boolean {
        return false
    }
}