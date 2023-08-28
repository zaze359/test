package com.zaze.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaze.common.base.ext.set
import com.zaze.common.thread.ThreadPlugins
import com.zaze.common.widget.CustomDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-05-06 - 11:27
 */
open class AbsViewModel : ViewModel() {
    @Deprecated("使用 flow 替代")
    val compositeDisposable = CompositeDisposable()

    /**
     * 拖拽刷新loading
     */
    val dragLoading = MutableLiveData<Boolean>()

    /**
     * 数据加载状态
     */
    val dataLoading = MutableLiveData<Boolean>()

    // --------------------------------------------------
    // obtain时已默认 observe
    /**
     * 提示信息
     */
    internal val _showMessage = SingleLiveEvent<String>()
    protected val showMessage = _showMessage

    /**
     * 信息提示
     */
    internal val _progress = SingleLiveEvent<String>()
    protected val progress = _progress

    /**
     * 是否退出
     * finish()
     */
    internal val _finish = SingleLiveEvent<Void>()
    protected val finish = _finish

    /**
     * 弹窗提示
     */
    internal val _tipDialog =
        SingleLiveEvent<CustomDialog.Builder>()
    protected val tipDialog = _tipDialog

    // --------------------------------------------------
    /**
     * 显示进度
     */
    fun showProgress(string: String = "") {
        progress.set(string)
    }

    /**
     * 隐藏进度
     */
    fun hideProgress(delay: Long = 0) {
        if (delay > 0) {
            ThreadPlugins.runInUIThread(Runnable {
                progress.value = null
            }, delay)
        } else {
            progress.set(null)
        }
    }

    fun toastMessage(message: String) {
        showMessage.set(message)
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