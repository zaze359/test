package com.zaze.common.widget.loading

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes

/**
 * Description : 自定义加载 view
 *
 * @author zaze
 * @version : 2019-02-12 - 15:11
 */
abstract class LoadingView(context: Context, @LayoutRes val layoutRes: Int) : LinearLayout(context) {

    companion object {
        /**
         * 默认提示文字
         */
        const val DEFAULT_TIP_TEXT = "加载中"

        /**
         * 垂直加载动画
         * [context] context
         */
        @JvmStatic
        fun createVerticalLoading(context: Context): LoadingView {
            return VerticalLoading(context)
        }

        /**
         * 水平加载动画
         * [context] context
         */
        @JvmStatic
        fun createHorizontalLoading(context: Context): LoadingView {
            return HorizontalLoading(context)
        }
    }

    init {
        createRootView(context)
    }

    /**
     * 构建RootView
     * [context] context context
     */
    private fun createRootView(context: Context) {
        initView(LayoutInflater.from(context).inflate(layoutRes, this, true))
    }

    /**
     * 初始化子view
     * [root] rootView
     */
    abstract fun initView(root: View)

    /**
     * 设置文字内容
     * [text] text 文字信息
     * @return LoadingView
     */
    abstract fun setText(text: String?): LoadingView

    /**
     * 设置文字颜色
     * [colorRes] colorRes 文字颜色 R.color.x
     * @return LoadingView
     */
    abstract fun setTextColor(@ColorInt colorRes: Int): LoadingView
    // --------------------------------------------------
    /**
     * 开始动画
     */
    abstract fun startAnimation()

    /**
     * 停止动画
     */
    abstract fun stopAnimation()

    private var mListener: Animation.AnimationListener? = null

    fun setAnimationListener(listener: Animation.AnimationListener) {
        mListener = listener
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        mListener?.onAnimationStart(animation)
    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        mListener?.onAnimationEnd(animation)
    }

}
