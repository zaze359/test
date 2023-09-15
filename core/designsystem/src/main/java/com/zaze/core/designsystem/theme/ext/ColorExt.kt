package com.zaze.core.designsystem.theme.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.zaze.core.designsystem.util.ColorUtil
import com.zaze.core.designsystem.util.ThemeUtils


fun Context.surfaceColor() = resolveColor(com.google.android.material.R.attr.colorSurface, Color.WHITE)

fun Fragment.surfaceColor() = resolveColor(com.google.android.material.R.attr.colorSurface, Color.WHITE)

fun Context.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ThemeUtils.resolveColor(this, attr, fallBackColor)

fun Fragment.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ThemeUtils.resolveColor(requireContext(), attr, fallBackColor)
@CheckResult
fun Drawable.tint(@ColorInt color: Int): Drawable {
    val tintedDrawable = DrawableCompat.wrap(this).mutate()
    setTint(color)
    return tintedDrawable
}

@CheckResult
fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable =
    tint(context.getColorCompat(color))

@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

inline val @receiver:ColorInt Int.isColorLight
    get() = ColorUtil.isColorLight(this)

inline val @receiver:ColorInt Int.lightColor
    get() = ColorUtil.withAlpha(this, 0.5F)

inline val @receiver:ColorInt Int.lighterColor
    get() = ColorUtil.lightenColor(this)

inline val @receiver:ColorInt Int.darkerColor
    get() = ColorUtil.darkenColor(this)

inline val Int.colorStateList: ColorStateList
    get() = ColorStateList.valueOf(this)

fun @receiver:ColorInt Int.addAlpha(alpha: Float): Int {
    return ColorUtil.withAlpha(this, alpha)
}
//

fun Button.accentTextColor() {
//    setTextColor(context.accentColor())
}