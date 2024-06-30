package com.zaze.core.designsystem.theme

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 15:59
 */
object TintManager {

    fun getColorStateList(@ColorInt color: Int): ColorStateList? {
        if (color == 0) return null
        return ColorStateList.valueOf(color)
    }

    fun getColorStateList(context: Context, @AttrRes attrRes: Int): ColorStateList? {
        if (attrRes == 0) {
            return null
        }
        val typedArray = context.obtainStyledAttributes(intArrayOf(attrRes))
        return typedArray.getColorStateList(0)

//        val colorRes = attrToColorId(context, attrRes)
//        typedArray.recycle()
//        if (colorRes == 0) {
//            return null
//        }
//        return getColorStateList(colorRes)
    }

    fun attrToResourceId(context: Context, @AttrRes attrRes: Int): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attrRes))
        val resId = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        return resId
    }

    fun attrToColorId(context: Context, @AttrRes attrRes: Int): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }
}