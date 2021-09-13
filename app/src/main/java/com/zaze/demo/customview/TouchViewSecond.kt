package com.zaze.demo.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("TouchViewSecond", "dispatchTouchEvent: ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("TouchViewSecond", "onInterceptTouchEvent: ${ev?.action}")
//        return true
        return super.onInterceptTouchEvent(ev)
//        return MotionEvent.ACTION_DOWN == ev?.action
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("TouchViewSecond", "onTouchEvent: ${event?.action}")
//        return super.onTouchEvent(event)
        return true
    }

//    fun aaa(event: MotionEvent?): Boolean {
//        return MotionEvent.ACTION_DOWN == event?.action
//    }
}