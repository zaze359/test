package com.zaze.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.*

/**
 * Description : 转换工具类

 * @author zaze
 * *
 * @version 2017/8/26 - 下午4:53 1.0
 */
object ZConvertUtil {
    // --------------------------------------------------
    // --------------------------------------------------
    fun objectToByte(obj: Serializable?): ByteArray? {
        var bytes: ByteArray? = null
        if (obj != null) {
            try {
                val bo = ByteArrayOutputStream()
                val oo = ObjectOutputStream(bo)
                oo.writeObject(obj)
                bytes = bo.toByteArray()

                bo.close()
                oo.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bytes
    }

    fun byteToObject(bytes: ByteArray): Any? {
        var obj: Any? = null
        try {
            val bi = ByteArrayInputStream(bytes)
            val oi = ObjectInputStream(bi)
            obj = oi.readObject()
            bi.close()
            oi.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }
    // --------------------------------------------------
    /**
     * [bitmap] bitmap
     * [format] Bitmap.CompressFormat
     * @return ByteArray
     */
    fun bitmap2Bytes(bitmap: Bitmap?, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, 100, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * [bytes] 字节数组
     * @return bitmap
     */
    fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
        return if (bytes == null || bytes.isEmpty()) {
            null
        } else {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    // --------------------------------------------------
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

    fun bitmap2Drawable(res: Resources, bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) {
            null
        } else {
            BitmapDrawable(res, bitmap)
        }
    }
    // --------------------------------------------------
    /**
     * [drawable] drawable
     * [format] Bitmap.CompressFormat
     * @return ByteArray
     */
    fun drawable2Bytes(drawable: Drawable?, format: Bitmap.CompressFormat): ByteArray? {
        return if (drawable == null) {
            null
        } else {
            bitmap2Bytes(drawable2Bitmap(drawable), format)
        }
    }

    /**
     * [res]Resources
     * @return drawable
     */
    fun bytes2Drawable(res: Resources, bytes: ByteArray): Drawable? {
        return bitmap2Drawable(res, bytes2Bitmap(bytes))
    }
}
