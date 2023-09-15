package com.zaze.demo.component.floating

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.zaze.utils.DisplayUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * 悬浮窗view
 */
class FloatingLayout : FrameLayout {

    private val mParams: WindowManager.LayoutParams
    private val mWindowManager: FloatingManager

    /** 是否吸附到两边 */
    var isSnap = false
    private var mTouchStartX = 0F
    private var mTouchStartY = 0F

    private var _viewWidth: Int = 0
    private var _viewHeight: Int = 0
    private val viewWidth: Int
        get() = if (isPortrait()) {
            _viewWidth
        } else {
            _viewHeight
        }
    private val viewHeight: Int
        get() = if (isPortrait()) {
            _viewHeight
        } else {
            _viewWidth
        }
    private val screenWidth: Int
        /**
         * 得到屏幕宽度
         *
         * @return 屏幕宽度
         */
        get() = if (isPortrait()) {
            DisplayUtil.getDisplayProfile().widthPixels
        } else {
            DisplayUtil.getDisplayProfile().heightPixels
        }
    private val screenHeight: Int
        /**
         * 得到屏幕高度
         *
         * @return 屏幕高度
         */
        get() = if (isPortrait()) {
            DisplayUtil.getDisplayProfile().heightPixels
        } else {
            DisplayUtil.getDisplayProfile().widthPixels
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        mWindowManager = FloatingManager.getInstance(context)
        mParams = WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mParams.gravity = Gravity.START or Gravity.TOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TYPE_SYSTEM_OVERLAY, TYPE_SYSTEM_ERROR
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888
        mParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//        mParams.width = viewWidth
//        mParams.height = viewHeight
        this.isVisible = false
        mWindowManager.addView(this, mParams)
    }

//    override fun addView(child: View?) {
//        super.addView(child)
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 0) { // 取第一个 child 作为宽高
            val child = getChildAt(0)
            measureChild(
                child,
                widthMeasureSpec,
                heightMeasureSpec
            )
            _viewWidth = child.measuredWidth
            _viewHeight = child.measuredHeight
            setMeasuredDimension(child.measuredWidth, child.measuredHeight)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
//        ZLog.i(ZTag.TAG, "floating: _viewWidth:$_viewWidth; _viewHeight:$_viewHeight")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        ZLog.i(ZTag.TAG, "floating onLayout :$screenWidth; $width")
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            updateView()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                if (event.pointerCount != 1) {
                    return false
                }
                mTouchStartX = event.rawX
                mTouchStartY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                updatePosition(event.rawX - mTouchStartX, event.rawY - mTouchStartY)
                mTouchStartX = event.rawX
                mTouchStartY = event.rawY
            }

            MotionEvent.ACTION_UP -> {
                isPressed = false
                when {
                    !isSnap -> { // 不吸附，跳过
                    }

                    mParams.x > screenWidth / 2 - viewWidth / 2 -> {
                        sideStop(screenWidth - mParams.x)
                    }

                    else -> {
                        sideStop(-mParams.x)
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                isPressed = false
            }

            else -> {}
        }
        return true
    }

//    override fun performClick(): Boolean {
//        return super.performClick()
//    }

    /**
     * 将控件添加到windowManager中
     */
    fun show() {
        ZLog.i(ZTag.TAG, "floating: show")
        updateView()
        this.isVisible = true
    }

    private fun updateView() {
        mParams.x = screenWidth - viewWidth
        mParams.y = screenHeight / 2 - viewHeight / 2
        if (viewWidth > 0 && height > 0) {
            mParams.width = viewWidth
            mParams.height = viewHeight
        }
        ZLog.i(ZTag.TAG, "floating mParams: $mParams")
        mWindowManager.updateView(this, mParams)
    }

    /**
     * 将控件从windowManager移除
     */
    fun hide() {
        mWindowManager.removeView(this)
    }

    private fun isPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 放手，控件移动到边缘
     *
     * @param offset 移动距离
     */
    private fun sideStop(offset: Int) {
        val valueAnimator = ValueAnimator.ofInt(0, offset)
        valueAnimator.addUpdateListener { animation ->
            mParams.x += animation.animatedValue as Int
            mWindowManager.updateView(this@FloatingLayout, mParams)
        }
        valueAnimator.start()
    }

    /**
     * 更新控件位置
     *
     * @param offsetX 偏移量x
     * @param offsetY 偏移量y
     */
    private fun updatePosition(offsetX: Float, offsetY: Float) {
        when {
            mParams.x + offsetX < 0 -> { // 超出边界
                mParams.x = 0
            }

            mParams.x + offsetX > screenWidth - viewWidth -> {
                mParams.x = screenWidth - viewWidth
            }

            else -> {
                mParams.x += offsetX.toInt()
            }
        }

        when {
            mParams.y + offsetY < 0 -> {
                mParams.y = 0
            }

            mParams.y > screenHeight - viewHeight -> {
                mParams.y = screenHeight - viewHeight
            }

            else -> {
                mParams.y += offsetY.toInt()
            }
        }
        mWindowManager.updateView(this, mParams)
    }
}