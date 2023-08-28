package com.zaze.feature.sliding.conflict

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


/**
 * 禁用内部子View 的滑动
 */
internal class NoScrollViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 不再将事件向 子View分发，可以禁用 ViewPager等控件的滑动
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

}