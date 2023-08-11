package com.zaze.ui.theme

import android.content.res.ColorStateList

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-24 - 14:52
 */
class TintInfo {
    var mTintList: ColorStateList? = null
    var mHasTintList = false

    fun clear() {
        mTintList = null
    }

}