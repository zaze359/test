package com.zaze.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaze.common.base.ext.set
import com.zaze.common.widget.CustomDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-05-06 - 11:27
 */
open class AbsViewModel : ViewModel() {
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
    fun showProgress(string: String?) {
        progress.set(string)
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
}