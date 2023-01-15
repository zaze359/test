package com.zaze.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zaze.common.R
import com.zaze.common.widget.loading.LoadingDialog
import com.zaze.common.widget.loading.LoadingView
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-30 - 00:00
 */
abstract class AbsFragment : Fragment() {

    companion object {
        var showLifeCycle = false
        private const val TAG = "LifeCycle"
    }

    val fragmentName = "${this.javaClass.simpleName}@${Integer.toHexString(this.hashCode())}"


    private val loadingDialog: LoadingDialog? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        context?.let {
            LoadingDialog(it, createLoadingView())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showLifeCycle)
            ZLog.i(TAG, "$fragmentName onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        super.onDestroy()
    }

    /**
     * 构建loadingView
     * @return LoadingView
     */
    open fun createLoadingView(): LoadingView {
        return LoadingView.createHorizontalLoading(requireContext()).apply {
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }
    // --------------------------------------------------

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
}