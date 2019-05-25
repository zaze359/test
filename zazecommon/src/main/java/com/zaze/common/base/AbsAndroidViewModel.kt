package com.zaze.common.base

import android.app.Application
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zaze.common.base.ext.set
import com.zaze.common.widget.CustomDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * Description : 基础AndroidViewModel
 * @author : ZAZE
 * @version : 2018-12-01 - 20:50
 */
abstract class AbsAndroidViewModel(application: Application) : AndroidViewModel(application) {
    val compositeDisposable = CompositeDisposable()
    /**
     * 错误提示信息
     */
    val errorMessage = MutableLiveData<String>()

    /**
     * 拖拽刷新loading
     */
    val dragLoading = MutableLiveData<Boolean>()

    /**
     * 数据加载状态
     */
    val dataLoading = MutableLiveData<Boolean>()
    /**
     * 信息提示
     */
    val progress = MutableLiveData<String>()

    /**
     * 是否退出
     * finish()
     */
    val finish = MutableLiveData<Void>()

    /**
     * 弹窗提示
     */
    val tipDialog = MutableLiveData<CustomDialog.Builder>()


    /**
     * 显示进度
     */
    fun showProgress(string: String) {
        progress.set(string)
    }

    fun showProgress(@StringRes stringRes: Int) {
        progress.set(getString(stringRes))
    }

    /**
     * 隐藏进度
     */
    fun hideProgress() {
        progress.set(null)
    }

    /**
     * 是否处于刷新中
     */
    open fun isLoading(): Boolean {
        return dataLoading.value == true || dragLoading.value == true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * StringRes资源转换String
     */
    fun getString(@StringRes stringRes: Int): String {
        return getApplication<Application>().getString(stringRes)
    }

    /**
     * StringRes资源转换String
     */
    fun getStringArray(@ArrayRes arrayRes: Int): Array<String> {
        return getApplication<Application>().resources.getStringArray(arrayRes)
    }
}