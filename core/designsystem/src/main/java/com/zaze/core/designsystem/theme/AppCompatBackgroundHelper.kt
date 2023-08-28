package com.zaze.core.designsystem.theme

import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.zaze.core.designsystem.R
import com.zaze.core.designsystem.theme.ext.BackgroundExtensible

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 16:01
 */
class AppCompatBackgroundHelper(view: View) :
    AppCompatBaseHelper<View>(view), BackgroundExtensible {
    private val mTintInfo = TintInfo()

    override fun loadFromAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        mView.context.withStyledAttributes(
            attrs,
            R.styleable.TintViewBackgroundHelper,
            defStyleAttr,
            0
        ) {
            if (hasValue(R.styleable.TintViewBackgroundHelper_backgroundTint)) {
                val mBackgroundTintResId =
                    getResourceId(R.styleable.TintViewBackgroundHelper_backgroundTint, -1)
                setBackgroundTintList(mBackgroundTintResId)
            }
        }
    }

    override fun tint() {
        if (mTintInfo.mHasTintList) {
            TintManager.getColorStateList(mView.context, R.attr.backgroundTint)
                ?.let { colorStateList ->
                    mTintInfo.mTintList = colorStateList
                }
        }
        applySupportBackgroundTint()
    }

    override fun setBackgroundTintList(resId: Int) {
        if (resId <= 0) {
            return
        }
        mTintInfo.mHasTintList = true
        mTintInfo.mTintList = ContextCompat.getColorStateList(mView.context, resId)
        applySupportBackgroundTint()
    }

    private fun applySupportBackgroundTint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mView.backgroundTintList = mTintInfo.mTintList
        } else {
            applySupportTint(mView.background, mTintInfo)
        }
    }
}
