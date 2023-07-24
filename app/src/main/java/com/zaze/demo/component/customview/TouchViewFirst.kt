package com.zaze.demo.component.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-03-25 - 20:47
 */
class TouchViewFirst : LinearLayout {

    private var touchX = 0f
    private var touchY = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.i("TouchViewFirst", "dispatchTouchEvent: ${ev.action}")
        return super.dispatchTouchEvent(ev)
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                if () { // 子元素不需要，交给父元素处理
//                    parent.requestDisallowInterceptTouchEvent(false)
//                }
//            }
//            else -> {
//            }
//        }
//        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i("TouchViewFirst", "onInterceptTouchEvent: ${ev.action}")
//        when(ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                touchX = ev.x
//                touchY = ev.y
//                return false
//            }
//            MotionEvent.ACTION_MOVE -> {
//                offsetX = ev.x - touchX
//                offsetY = ev.y - touchY
//                return if() {
//                     true
//                } else {
//                    false
//                }
//            }
//            else -> {
//                return false
//            }
//        }
//        return super.onInterceptTouchEvent(ev)
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("TouchViewFirst", "onTouchEvent: ${event.action}")
        return super.onTouchEvent(event)
    }
}