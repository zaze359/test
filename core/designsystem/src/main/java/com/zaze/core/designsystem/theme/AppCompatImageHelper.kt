package com.zaze.core.designsystem.theme

import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.zaze.core.designsystem.R
import com.zaze.core.designsystem.theme.ext.ImageExtensible

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 16:01
 */
class AppCompatImageHelper(view: ImageView) : AppCompatBaseHelper<ImageView>(view),
    ImageExtensible {
    //    private var mImageTintResId = 0
    private val mImageTintInfo = TintInfo()

    init {

    }

    override fun loadFromAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        val array = mView.context.obtainStyledAttributes(
                attrs,
                R.styleable.TintViewImageHelper,
                defStyleAttr,
                0
        )
        if (array.hasValue(R.styleable.TintViewImageHelper_imageTint)) {
            val mImageTintResId =
                    array.getResourceId(R.styleable.TintViewImageHelper_imageTint, -1)
            setImageTintList(mImageTintResId)
        }
        array.recycle()
    }

    override fun tint() {
        if (mImageTintInfo.mHasTintList) {
            TintManager.getColorStateList(mView.context, R.attr.imageTint)?.let { colorStateList ->
                mImageTintInfo.mTintList = colorStateList
            }
        }
        applySupportImageTint()
    }

    private fun applySupportImageTint() {
        mImageTintInfo.mTintList?.let {
            ImageViewCompat.setImageTintList(mView, it)
        }
    }
    // --------------------------------------------------

    override fun setImageTintList(resId: Int) {
        if (resId <= 0) {
            return
        }
        mImageTintInfo.mHasTintList = true
        mImageTintInfo.mTintList = ContextCompat.getColorStateList(mView.context, resId)
        applySupportImageTint()
    }
    // --------------------------------------------------
}