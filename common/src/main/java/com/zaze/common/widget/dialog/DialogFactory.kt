package com.zaze.common.widget.dialog

import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import android.view.Gravity
import android.view.View
import com.zaze.common.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-03-06 - 14:35
 */
class DialogFactory private constructor() {

    class Builder {
        @StyleRes
        var theme = R.style.TransparentDialog
        var cancelable = false
        var applicationOverlay = false
        var message: CharSequence? = null
        var messageGravity: Int = Gravity.CENTER
        var positive: String? = null
        var positiveListener: ((v: View) -> Unit)? = null
        var negative: String? = null
        var negativeListener: ((v: View) -> Unit)? = null
        var title: String? = null
        var tag: String? = null
        var extras: Bundle? = null

        fun theme(@StyleRes theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun cancelable(boolean: Boolean = true): Builder {
            this.cancelable = boolean
            return this
        }

        fun applicationOverlay(boolean: Boolean = true): Builder {
            this.applicationOverlay = boolean
            return this
        }

        fun message(message: CharSequence?, gravity: Int = Gravity.CENTER): Builder {
            this.message = message
            this.messageGravity = gravity
            return this
        }

        fun positive(
            positive: String = "确定",
            positiveListener: ((v: View) -> Unit)? = null
        ): Builder {
            this.positive = positive
            this.positiveListener = positiveListener
            return this
        }

        fun negative(
            negative: String = "取消",
            negativeListener: ((v: View) -> Unit)? = null
        ): Builder {
            this.negative = negative
            this.negativeListener = negativeListener
            return this
        }

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun extras(bundle: Bundle): Builder {
            this.extras = bundle
            return this
        }

        fun build(viewHolder: DialogViewHolder = CustomDialogHolder(this)): CustomDialogFragment {
            return CustomDialogFragment().init(viewHolder)
        }

        fun buildCustomDialog(
            context: Context,
            viewHolder: DialogViewHolder = CustomDialogHolder(this)
        ): CustomDialog {
            return CustomDialog(context, viewHolder)
        }
    }
}