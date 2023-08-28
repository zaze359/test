package com.zaze.feature.sliding.conflict

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

/**
 * 用于处理 ViewPager2 嵌套 ViewPager RecyclerView等导致的滑动冲突问题。
 */
class NestedScrollViewHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        // 不再将事件向 子View分发，可以禁用 ViewPager等控件的滑动
//        return false
//    }

    var tempX: Float = 0F
    var tempY: Float = 0F

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!canChildScroll(1F) && !canChildScroll(-1F)) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                tempX = ev.x
                tempY = ev.y
                // 预先置为不允许父容器拦截
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                // 计算滑动距离
                val offsetX = ev.x - tempX
                val offsetY = ev.y - tempY
                when {
                    canChildScroll(if (parentOrientation == RecyclerView.VERTICAL) offsetY else offsetX) -> {
                        // 子元素需要，不允许父容器拦截
                        parent.requestDisallowInterceptTouchEvent(true)
                    }

                    else -> {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

}

/**
 * 判断子元素是否需要滑动
 */
fun NestedScrollViewHost.canChildScroll(offset: Float): Boolean {
    return when (parentOrientation) {
        RecyclerView.VERTICAL -> { // 垂直方向滑动
            // 传入的是坐标的偏移，向上滑动时 > 0，向下滑动时 < 0。所以这里需要取反
            // direction < 0: 向上滑动
            // direction > 0: 向下滑动
            child?.canScrollVertically(-offset.toInt()) ?: false
        }

        else -> { // 水平方向滑动
            // 传入的是坐标的偏移，向左滑动时 > 0，向右滑动时 < 0。所以这里需要取反
            // direction < 0: 向左
            // direction > 0: 向右
            child?.canScrollHorizontally(-offset.toInt()) ?: false
        }
    }
}


val NestedScrollViewHost.child
    get() = if (childCount > 0) {
        getChildAt(0)
    } else {
        null
    }

val NestedScrollViewHost.parentOrientation: Int?
    get() {
        return parentViewPager2?.orientation
    }

val NestedScrollViewHost.parentViewPager: ViewPager?
    get() {
        var parentView = this.parent
        while (parentView != null && parentView !is ViewPager) {
            parentView = parentView.parent
        }
        return parentView as? ViewPager
    }

val NestedScrollViewHost.parentViewPager2: ViewPager2?
    get() {
        var parentView = this.parent
        while (parentView != null && parentView !is ViewPager2) {
            parentView = parentView.parent
        }
        return parentView as? ViewPager2
    }