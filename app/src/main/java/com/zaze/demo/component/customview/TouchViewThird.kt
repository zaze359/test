package com.zaze.demo.component.customview

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
class TouchViewThird : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e("TouchViewThird", "dispatchTouchEvent: ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.e("TouchViewThird", "onInterceptTouchEvent: ${ev.action}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("TouchViewThird", "onTouchEvent: ${event.action}")
        return super.onTouchEvent(event)
    }
}