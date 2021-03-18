package com.zaze.demo.theme

import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.zaze.demo.R
import com.zaze.demo.theme.ext.BackgroundExtensible

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
        val array = mView.context.obtainStyledAttributes(
                attrs,
                R.styleable.TintViewBackgroundHelper,
                defStyleAttr,
                0
        )
        if (array.hasValue(R.styleable.TintViewBackgroundHelper_backgroundTint)) {
            val mBackgroundTintResId =
                    array.getResourceId(R.styleable.TintViewBackgroundHelper_backgroundTint, -1)
            setBackgroundTintList(mBackgroundTintResId)
        }
        array.recycle()
    }

    override fun tint() {
        if (mTintInfo.mHasTintList) {
            TintManager.getColorStateList(mView.context, R.attr.backgroundTint)?.let { colorStateList ->
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
