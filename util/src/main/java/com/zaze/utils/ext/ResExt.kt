package com.zaze.utils.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

/**
 * Description :
 * @author : zaze
 * @version : 2022-12-10 20:33
 */

/**
 * R.dimen.xx 转 px Int
 */
fun @receiver:DimenRes Int.getDimen(context: Context): Int {
    return context.resources.getDimensionPixelSize(this)
}

/**
 * R.arrays.xx 转 Array<String>
 */
fun @receiver:ArrayRes Int.getStringArray(context: Context): Array<String> {
    return context.resources.getStringArray(this)
}

/**
 * R.string.xxx 转 String
 */
fun @receiver:StringRes Int.getString(context: Context): String {
    return context.getString(this)
}

fun @receiver:StringRes Int.getString(context: Context, vararg args: Any?): String {
    return context.getString(this, *args)
}

/**
 * R.color.xx 转 ColorInt
 */
@ColorInt
fun @receiver:ColorRes Int.getColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

fun @receiver:ColorRes Int.getColorStateList(context: Context): ColorStateList? {
    return ContextCompat.getColorStateList(context, this)
}

/**
 * R.drawable.xxx 转 Drawable
 */
fun @receiver:DrawableRes Int.getDrawable(context: Context): Drawable? {
    return AppCompatResources.getDrawable(context, this)
}

/**
 * R.drawable.xxx 转 Bitmap
 */
fun @receiver:DrawableRes Int.getBitmap(context: Context): Bitmap? {
    return BitmapFactory.decodeResource(context.resources, this)
}


