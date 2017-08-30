package com.zaze.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description : 转换工具类

 * @author zaze
 * *
 * @version 2017/8/26 - 下午4:53 1.0
 */
class ZConvertUtil {
    fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable != null) {
            try {
                bitmap = (drawable as BitmapDrawable).bitmap
            } catch (e: Throwable) {
                ZLog.e(ZTag.TAG_DEBUG, e.message)
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight
                val config = if (drawable.opacity != PixelFormat.OPAQUE) {
                    Bitmap.Config.ARGB_8888
                } else {
                    Bitmap.Config.RGB_565
                }
                bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
            }

        }
        return bitmap
    }
}
