package com.zaze.demo.component.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import androidx.annotation.RequiresApi

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-03-25 - 20:47
 */
class TouchViewSecond : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("TouchViewSecond", "dispatchTouchEvent: ${ev.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d("TouchViewSecond", "onInterceptTouchEvent: ${ev.action}")
        return true
//        return super.onInterceptTouchEvent(ev)
//        return MotionEvent.ACTION_DOWN == ev?.action
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("TouchViewThird", "onTouchEvent: ${event.action}")
//        Log.d("TouchViewSecond", "onTouchEvent: ${event?.action}  >> (${event?.x}, ${event?.y}); (${event?.rawX}, ${event?.rawY})")
//        Log.d("TouchViewSecond", "onTouchEvent: ${event?.action}  >> (${this.x}, ${this.y}); (${this.translationX}, ${this.translationY})")
        return super.onTouchEvent(event)
//        return true
    }

//    fun aaa(event: MotionEvent?): Boolean {
//        return MotionEvent.ACTION_DOWN == event?.action
//    }
}