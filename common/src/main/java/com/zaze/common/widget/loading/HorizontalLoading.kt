package com.zaze.common.widget.loading

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
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
internal class HorizontalLoading(context: Context) : LoadingView(context, R.layout.loading_horizontal_layout) {
    private lateinit var loadingMessage: TextView
    private lateinit var loadingImg: ImageView
    private lateinit var loadingAnimation: RotateAnimation

    override fun initView(root: View) {
        loadingMessage = root.findViewById(R.id.horizontal_loading_message)
        loadingImg = root.findViewById(R.id.horizontal_loading_img)
        //
        val magnify = 10000
        val toDegrees = 360F * magnify
        val duration = 400L * magnify
        loadingAnimation = RotateAnimation(0F, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F)
        loadingAnimation.duration = duration
        loadingAnimation.interpolator = LinearInterpolator()
        loadingAnimation.repeatCount = Animation.INFINITE
        loadingAnimation.repeatMode = Animation.RESTART
    }

    // --------------------------------------------------
    override fun setText(text: String?): HorizontalLoading {
        loadingMessage.text = text
        return this
    }

    override fun setTextColor(@ColorInt colorRes: Int): HorizontalLoading {
        loadingMessage.setTextColor(colorRes)
        return this
    }

    override fun startAnimation() {
        loadingImg.startAnimation(loadingAnimation)
    }

    override fun stopAnimation() {
//        loadingAnimation.cancel()
    }

}
