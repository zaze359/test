package com.zaze.demo.feature.image

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class FillWidthImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        scaleType = ScaleType.FIT_XY
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) {
            return  // couldn't resolve the URI
        }

        if (drawable.intrinsicWidth == 0 || drawable.intrinsicWidth == 0) {
            return  // nothing to draw (empty bounds)
        }
        val scale = 1.0F * width / drawable.intrinsicWidth
        drawable.setBounds(0, 0, width, (drawable.intrinsicHeight * scale).toInt())

        super.onDraw(canvas)
    }

}