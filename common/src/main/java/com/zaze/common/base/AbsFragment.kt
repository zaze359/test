package com.zaze.common.base

import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zaze.common.R
import com.zaze.common.widget.loading.LoadingDialog
import com.zaze.common.widget.loading.LoadingView
import com.zaze.utils.ToastUtil

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-30 - 00:00
 */
abstract class AbsFragment : Fragment() {

    private val loadingDialog: LoadingDialog? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        context?.let {
            LoadingDialog(it, createLoadingView())
        }
    }

    /**
     * 构建loadingView
     * @return LoadingView
     */
    open fun createLoadingView(): LoadingView {
        return LoadingView.createHorizontalLoading(context!!).apply {
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(content: String?) {
        ToastUtil.toast(context, content)
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

    // --------------------------------------------------

    fun progress(isShow: Boolean?) {
        progress(if (isShow == null || !isShow) null else LoadingView.DEFAULT_TIP_TEXT)
    }

    fun progress(message: String? = null) {
        if (message == null) {
            loadingDialog?.dismiss()
        } else {
            loadingDialog?.setText(message)?.show()
        }
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        super.onDestroy()
    }
}