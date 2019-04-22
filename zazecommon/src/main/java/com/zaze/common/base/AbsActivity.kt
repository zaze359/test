package com.zaze.common.base

import android.os.Bundle
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import com.zaze.common.R
import com.zaze.common.base.ext.setImmersion
import com.zaze.utils.ZTipUtil

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-30 - 15:48
 */
abstract class AbsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        setImmersion(isFullScreen(), getStatusBarColor())
    }

    /**
     * onCreate(savedInstanceState)中的初始化
     * [savedInstanceState] savedInstanceState
     */
    abstract fun init(savedInstanceState: Bundle?)

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

    // --------------------------------------------------

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(content: String?) {
        ZTipUtil.toast(this, content)
    }
    // --------------------------------------------------

    /**
     * 读取dimen 转 px
     */
    fun getDimen(@DimenRes resId: Int): Int {
        return this.resources.getDimensionPixelSize(resId)
    }

    /**
     * arrays.xml 转数据
     */
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return this.resources.getStringArray(resId)
    }
}

