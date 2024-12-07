package com.zaze.demo.component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.customview.widget.ViewDragHelper
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag


class DragContainer : CoordinatorLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            // 处理可拖拽的 child
            return child is Draggable
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            // 处理水平可拖动位置
            val leftBound = paddingLeft
            val rightBound = width - child.width - paddingRight
            // 最小: leftBound； 最大: rightBound
            return minOf(maxOf(left, leftBound), rightBound)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            // 处理垂直方向可拖动位置
            return top
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return super.getViewHorizontalDragRange(child)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return super.getViewVerticalDragRange(child)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            // 手指释放时
            viewDragHelper.settleCapturedViewAt(100, 100)
            invalidate()
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            ZLog.i(ZTag.TAG_DEBUG, "onViewPositionChanged: ${left}, $top; $dx, $dy")
            super.onViewPositionChanged(changedView, left, top, dx, dy)
        }
    }

    private val viewDragHelper: ViewDragHelper = ViewDragHelper.create(this, 1.0F, callback)


    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }
}