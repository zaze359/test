package com.zaze.common.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.annotation.StyleRes
import com.zaze.common.R
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-03-06 - 14:35
 */
class CustomDialog(context: Context, val builder: Builder) {
    private val dialog = Dialog(context, builder.style)
    private val message: String?
    private val positive: String?
    private val negative: String?

    init {
        message = builder.message
        positive = builder.positive
        negative = builder.negative
        (builder.custom ?: createView(context, builder.callback)).let {
            dialog.setContentView(it)
        }
        dialog.setCancelable(builder.cancelable)
    }

    private fun createView(context: Context, callback: CallBack?): View {
//        val view: View
//        val messageTxt: TextView
//        val sureBtn: Button
//        var cancelBtn: Button? = null
//        if (negative != null) {
//            view = View.inflate(context, R.layout.dialog_sure_cancle, null)
//            messageTxt = view.findViewById(R.id.dialog_message)
//            sureBtn = view.findViewById(R.id.dialog_sure_btn)
//            cancelBtn = view.findViewById(R.id.dialog_cancel_btn)
//        } else {
//            view = View.inflate(context, R.layout.dialog_sure, null)
//            messageTxt = view.findViewById(R.id.dialog_message)
//            sureBtn = view.findViewById(R.id.dialog_sure_btn)
//        }
//        view.apply {
//            message?.let {
//                messageTxt.text = it
//            }
//            negative?.let {
//                cancelBtn?.text = it
//            }
//            positive?.let {
//                sureBtn.text = it
//            }
//            sureBtn.setOnClickListener {
//                if (dialog.isShowing) {
//                    dialog.dismiss()
//                }
//                callback?.onPositive()
//            }
//            cancelBtn?.setOnClickListener {
//                if (dialog.isShowing) {
//                    dialog.dismiss()
//                }
//                callback?.onNegative()
//            }
//        }
        return View.inflate(context, R.layout.layout_empty_view, null)
    }

    fun show() {
        val context = dialog.context
        if (context is Activity && context.isFinishing) {
            ZLog.e(ZTag.TAG_ERROR, "Activity isFinishing return it!")
            return
        }
        dialog.show()
    }

    class Builder {
        @StyleRes
        internal var style = R.style.TransparentDialog
        internal var cancelable = false
        internal var custom: View? = null
        internal var message: String? = null
        internal var positive: String? = null
        internal var negative: String? = null
        internal var callback: CallBack? = null

        fun style(@StyleRes style: Int): Builder {
            this.style = style
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun message(message: String): Builder {
            this.message = message
            return this
        }

        fun positive(positive: String = "确定"): Builder {
            this.positive = positive
            return this
        }

        fun negative(negative: String = "取消"): Builder {
            this.negative = negative
            return this
        }

        fun custom(custom: View): Builder {
            this.custom = custom
            return this
        }

        fun callback(callback: CallBack): Builder {
            this.callback = callback
            return this
        }

        fun build(context: Context): CustomDialog {
            return CustomDialog(context, this)
        }
    }

    /**
     * dialog弹窗点击操作回调接口
     */
    abstract class CallBack {

        /**
         * 确认按钮点击回调
         */
        abstract fun onPositive()

        /**
         * 取消按钮点击回调
         */
        fun onNegative() {}

        /**
         * 中立按钮点击回调
         */
        fun onNeutral() {}
    }

}