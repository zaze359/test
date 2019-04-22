package com.zaze.common.base

import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import com.zaze.utils.ZTipUtil

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-30 - 00:00
 */
abstract class AbsFragment : Fragment() {

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(content: String?) {
        ZTipUtil.toast(context, content)
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