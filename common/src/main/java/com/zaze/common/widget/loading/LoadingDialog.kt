package com.zaze.common.widget.loading

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import com.zaze.common.R

/**
 * Description :  自定义加载 dialog
 * @author : ZAZE
 * @version : 2019-02-12 - 15:26
 */

class LoadingDialog {

    private lateinit var loadingDialog: Dialog
    private lateinit var loadingView: LoadingView

    private val dismissRunnable = Runnable {
        loadingDialog.window?.let {
            loadingView.stopAnimation()
            loadingDialog.dismiss()
        }
    }

    constructor(context: Context, loadingView: LoadingView = LoadingView.createVerticalLoading(context)) {
        this.loadingView = loadingView
        initDialog(context, loadingView)
    }

    companion object {
        const val MIN_TIME = 500L
    }

    /**
     * 初始化弹窗
     * [context] context context
     */
    private fun initDialog(context: Context, loadingView: LoadingView) {
        // 包裹一层, 防止发生内容频繁变化时dialog产生平移抖动的问题
        val contentView = LinearLayout(context).apply {
            gravity = Gravity.CENTER
            addView(loadingView)
        }
        loadingDialog = Dialog(context, R.style.LoadingDialog).apply {
            setContentView(contentView)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    /**
     * 设置文字内容
     * [text] text 文字信息
     */
    fun setText(text: String?): LoadingDialog {
        loadingDialog.window?.let {
            loadingView.setText(text)
        }
        return this
    }

    /**
     * 显示弹窗
     */
    fun show() {
        if (!loadingDialog.isShowing) {
            loadingView.startAnimation()
            loadingDialog.show()
            loadingDialog.window?.let {
                val attr = it.attributes
                attr.width = WindowManager.LayoutParams.MATCH_PARENT
                it.attributes = attr
            }
        }
    }

    /**
     * 隐藏弹窗
     */
    fun dismiss() {
        dismissRunnable.run()
    }
}