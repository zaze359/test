package com.zaze.core.designsystem.theme

import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.DrawableCompat
import com.zaze.core.designsystem.theme.TintInfo
import com.zaze.core.designsystem.theme.widgets.Tintable

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:57
 */
abstract class AppCompatBaseHelper<T : View>(val mView: T) : Tintable {

    private var mSkipNextApply = false

    protected open fun skipNextApply(): Boolean {
        if (mSkipNextApply) {
            mSkipNextApply = false
            return true
        }
        mSkipNextApply = true
        return false
    }

    protected open fun setSkipNextApply(flag: Boolean) {
        mSkipNextApply = flag
    }

    abstract fun loadFromAttribute(
            attrs: AttributeSet?,
            defStyleAttr: Int
    )

    fun applySupportTint(drawable: Drawable?, tintInfo: TintInfo?) {
        if (drawable == null || tintInfo?.mTintList == null) {
            return
        }
        DrawableCompat.setTintList(drawable, tintInfo.mTintList)
        drawable.invalidateSelf()
    }
}