package com.zaze.core.designsystem.theme.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import com.zaze.core.designsystem.theme.AppCompatBackgroundHelper
import com.zaze.core.designsystem.theme.AppCompatImageHelper
import com.zaze.core.designsystem.theme.ext.BackgroundExtensible
import com.zaze.core.designsystem.theme.ext.ImageExtensible

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:26
 */
@Deprecated("")
class TintImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), Tintable, ImageExtensible,
    BackgroundExtensible {
    private var appCompatImageHelper: AppCompatImageHelper? = null
    private var appCompatBackgroundHelper: AppCompatBackgroundHelper? = null

    init {
        if (!isInEditMode) {
            appCompatImageHelper = AppCompatImageHelper(this).apply {
                loadFromAttribute(attrs, defStyleAttr)
            }
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

