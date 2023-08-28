package com.zaze.core.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.google.android.material.color.MaterialColors
import com.zaze.core.designsystem.util.ColorUtil

class ColorIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        context.withStyledAttributes(attrs, R.styleable.ColorIconView, defStyleAttr, 0) {
            val iconBackgroundColor =
                getColor(R.styleable.ColorIconView_iconBackgroundColor, Color.RED)
            setIconBackgroundColor(iconBackgroundColor)
        }
    }

    fun setIconBackgroundColor(@ColorInt color: Int) {
        background = ContextCompat.getDrawable(context, R.drawable.bg_circle)
        val finalColor = MaterialColors.harmonize(
            color,
            color
        )
        backgroundTintList = ColorStateList.valueOf(ColorUtil.adjustAlpha(finalColor, 0.22f))
        imageTintList = ColorStateList.valueOf(ColorUtil.withAlpha(finalColor, 0.75f))
    }
}