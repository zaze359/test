package com.zaze.demo.debug

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-25 - 20:10
 */
class ValueSelectView : AppCompatTextView {
    var values: List<String> = ArrayList()
        set(value) {
            field = value
            selectedIndex = 0
            invalidate()
        }
    /**
     * 当前选中的位置
     */
    private var selectedIndex = 0
    /**
     * 可以偏移的行数
     */
    private var offsetCount = 1

    /**
     * 是否需要渐变
     */
    var needGradual = false
    var needLooper = false

    private val paintL: Paint
    private val paintH: Paint

    /**
     * 行间距
     */
    private val interval: Float
    /**
     *
     */
    private val fontOffsetCenter: Float
    /**
     *
     */
    private val fontOffsetOther: Float

    private var mOffset = 0F

    private var baseX = 0f
    private var baseY = 0f

    private var touchY = 0F

    private val defaultAlpha = 150

    var onValueChangedListener: OnValueChangedListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        paintH = createPaint().also {
            it.textSize = 46.0F
            it.color = Color.parseColor("#5B5B5B")
        }
        fontOffsetCenter = Math.abs(paintH.fontMetrics.top + paintH.fontMetrics.bottom)
        paintL = createPaint().also {
            it.textSize = 30.0F
            it.setARGB(255, 170, 170, 170)
        }
        fontOffsetOther = Math.abs(paintL.fontMetrics.top + paintL.fontMetrics.bottom)
        interval = fontOffsetCenter + 16
    }

    private fun createPaint(): Paint {
        return Paint().apply {
            textAlign = Paint.Align.CENTER
            color = Color.BLACK
            isDither = true
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (selectedIndex >= values.size) {
            return
        }
//        canvas.drawLine(baseX, baseY, baseX + 100, baseY, paint)
        canvas.translate(0f, mOffset)
        // 1. 绘制中间
        canvas.drawText(values[selectedIndex], baseX, baseY + fontOffsetCenter / 2, paintH)
        // 2. 绘制中点上方的item
        if (needGradual) {
            paintL.alpha = defaultAlpha
        }
        var aboveY = baseY
        val topBorder: Int
        val bottomBorder: Int
        if (needLooper) {
            topBorder = selectedIndex - Math.min(offsetCount, values.size)
            bottomBorder = selectedIndex + offsetCount
        } else {
            topBorder = Math.max(selectedIndex - offsetCount, 0)
            bottomBorder = Math.min(selectedIndex + offsetCount, values.size)
        }
        // --------------------------------------------------
        for (i in selectedIndex - 1 downTo topBorder) {
            if (i >= 0) {
                values[i]
            } else {
                values[i + values.size]
            }.apply {
                aboveY -= interval
                if (needGradual) {
                    paintL.alpha = Math.max(0, paintL.alpha - 50)
                }
                if (aboveY >= 0) {
//                    ZLog.i(ZTag.TAG_DEBUG, "aboveY : $aboveY")
                    canvas.drawText(this, baseX, aboveY, paintL)
                }
            }
        }
        // 2. 绘制中点下方的item
        if (needGradual) {
            paintL.alpha = defaultAlpha
        }
        var belowY = baseY
        for (i in selectedIndex + 1 until bottomBorder) {
            if (i >= values.size) {
                values[i % values.size]
            } else {
                values[i]
            }.apply {
                belowY += interval
                if (needGradual) {
                    paintL.alpha = Math.max(0, paintL.alpha - 50)
                }
                if (belowY <= height) {
//                    ZLog.i(ZTag.TAG_DEBUG, "belowY + fontOffsetOther : ${belowY + fontOffsetOther}")
                    canvas.drawText(this, baseX, belowY + fontOffsetOther, paintL)
                }
            }
        }
//        ZLog.i(ZTag.TAG_DEBUG, "interval  : $interval")
//        ZLog.i(ZTag.TAG_DEBUG, "fontOffsetOther  : $fontOffsetOther")
//        ZLog.i(ZTag.TAG_DEBUG, "fontOffsetCenter  : $fontOffsetCenter")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        baseX = width * 0.5f
        baseY = height * 0.5f
        offsetCount = 5
        setMeasuredDimension(width, height)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                mOffset = event.y - touchY
                if (canMove(mOffset)) {
                    if (mOffset >= interval / 2) {
                        // 下移一位
                        touchY = event.y
                        mOffset = 0F
                        selectedIndex--
                        if (selectedIndex < 0) {
                            selectedIndex += values.size
                        }
                        onValueChangedListener?.onValueChanged(values[selectedIndex], selectedIndex)
                    } else if (mOffset <= -interval / 2) {
                        // 上移一位
                        touchY = event.y
                        mOffset = 0F
                        selectedIndex++
                        if (selectedIndex >= values.size) {
                            selectedIndex %= values.size
                        }
                        onValueChangedListener?.onValueChanged(values[selectedIndex], selectedIndex)
                    }
                    invalidate()
                }
            }
            else -> {
                mOffset = 0F
                invalidate()
            }
        }
        return true
    }

    private fun canMove(offset: Float): Boolean {
        return if (needLooper) {
            values.size > 1
        } else {
            when {
                offset > 0 -> {
                    // 当前不是第一个时，可以下滑
                    selectedIndex != 0
                }
                offset < 0 -> {
                    // 当前不是最后一个时，可以上滑
                    selectedIndex != values.size - 1
                }
                else -> false
            }
        }
    }


    // --------------------------------------------------
    fun next() {
        selectedIndex++
        invalidate()
    }

    fun setSelect(index: Int) {
        selectedIndex = index
        invalidate()
    }

    fun getCenterValue(): String {
        return getValue(selectedIndex)
    }

    fun getValue(index: Int): String {
        return if (index < values.size) {
            values[index]
        } else {
            ""
        }
    }

    interface OnValueChangedListener {
        fun onValueChanged(value: String, index: Int)
    }
}