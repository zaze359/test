package com.zaze.demo.theme.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.zaze.demo.theme.AppCompatBackgroundHelper

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:26
 */
class TintConstraintLayout : ConstraintLayout, Tintable {
    private var appCompatBackgroundHelper: AppCompatBackgroundHelper? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        if (isInEditMode) {
            return
        }
        appCompatBackgroundHelper = AppCompatBackgroundHelper(this).apply {
            loadFromAttribute(attrs, defStyleAttr)
        }
    }

    override fun tint() {
        appCompatBackgroundHelper?.tint()
    }
}