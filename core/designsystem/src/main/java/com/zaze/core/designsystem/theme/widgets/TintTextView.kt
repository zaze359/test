package com.zaze.core.designsystem.theme.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zaze.core.designsystem.theme.AppCompatTextHelper

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 17:11
 */
@Deprecated("")

class TintTextView : AppCompatTextView, Tintable {
    private var appCompatTextHelper: AppCompatTextHelper? = null

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
        appCompatTextHelper = AppCompatTextHelper(this).apply {
            loadFromAttribute(attrs, defStyleAttr)
        }
    }

    override fun tint() {
        appCompatTextHelper?.tint()
    }
}