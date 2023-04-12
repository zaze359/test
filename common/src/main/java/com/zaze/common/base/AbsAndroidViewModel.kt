package com.zaze.common.base

import android.app.Application
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.zaze.common.base.ext.set

/**
 * Description : 基础AndroidViewModel
 * @author : ZAZE
 * @version : 2018-12-01 - 20:50
 */
open class AbsAndroidViewModel(val application: Application) : AbsViewModel() {

    fun showProgress(@StringRes stringRes: Int) {
        progress.set(getString(stringRes))
    }

    /**
     * StringRes资源转换String
     */
    fun getString(@StringRes stringRes: Int): String {
        return application.getString(stringRes)
    }

    /**
     * StringRes资源转换String
     */
    fun getStringArray(@ArrayRes arrayRes: Int): Array<String> {
        return application.resources.getStringArray(arrayRes)
    }
}