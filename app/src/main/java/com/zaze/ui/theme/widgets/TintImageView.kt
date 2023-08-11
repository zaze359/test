package com.zaze.ui.theme.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.zaze.ui.theme.AppCompatBackgroundHelper
import com.zaze.ui.theme.AppCompatImageHelper
import com.zaze.ui.theme.ext.BackgroundExtensible
import com.zaze.ui.theme.ext.ImageExtensible

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:26
 */
class TintImageView : AppCompatImageView, Tintable, ImageExtensible, BackgroundExtensible {
    private var appCompatImageHelper: AppCompatImageHelper? = null
    private var appCompatBackgroundHelper: AppCompatBackgroundHelper? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) {
            return
        }
        appCompatImageHelper = AppCompatImageHelper(this).apply {
            loadFromAttribute(attrs, defStyleAttr)
        }
    }

    override fun tint() {
        appCompatImageHelper?.tint()
        appCompatBackgroundHelper?.tint()
    }

    override fun setImageTintList(resId: Int) {
        appCompatImageHelper?.setImageTintList(resId)
    }

    override fun setBackgroundTintList(resId: Int) {
        appCompatBackgroundHelper?.setBackgroundTintList(resId)
    }
}

