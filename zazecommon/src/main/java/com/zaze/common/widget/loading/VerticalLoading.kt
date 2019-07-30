package com.zaze.common.widget.loading

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import com.zaze.common.R

/**
 * Description : 自定义加载 view
 *
 * @author zaze
 * @version : 2019-02-12 - 15:11
 */
internal class VerticalLoading(context: Context) : LoadingView(context, R.layout.loading_vertical_layout) {
    private lateinit var loadingMessage: TextView
    private lateinit var animationDrawable: AnimationDrawable

    override fun initView(root: View) {
        loadingMessage = root.findViewById(R.id.vertical_loading_message)
        animationDrawable = root.findViewById<ImageView>(R.id.vertical_loading_img).drawable as AnimationDrawable
        setText(DEFAULT_TIP_TEXT)
    }

    // --------------------------------------------------
    override fun setText(text: String?): VerticalLoading {
        loadingMessage.text = if (TextUtils.isEmpty(text)) {
            DEFAULT_TIP_TEXT
        } else {
            text
        }
        return this
    }

    override fun setTextColor(@ColorInt colorRes: Int): VerticalLoading {
        loadingMessage.setTextColor(colorRes)
        return this
    }

    override fun startAnimation() {
        animationDrawable.start()
    }

    override fun stopAnimation() {
        if (animationDrawable.isRunning) {
            animationDrawable.selectDrawable(0)
            animationDrawable.stop()
        }
    }
}
