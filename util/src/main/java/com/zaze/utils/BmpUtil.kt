package com.zaze.utils

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.media.ThumbnailUtils
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import com.zaze.utils.ext.toBytes
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlin.math.hypot


/**
 * Description : 转换工具类
 * @author zaze
 * *
 * @version 2017/8/26 - 下午4:53 1.0
 */

object BmpUtil {
    /**
     * [bitmap] bitmap
     * [format] Bitmap.CompressFormat
     * @return ByteArray
     */
    @JvmStatic
    fun bitmap2Bytes(
        bitmap: Bitmap?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): ByteArray? {
        return bitmap?.toBytes(format)
    }

    /**
     * [bytes] 字节数组
     * @return bitmap
     */
    @JvmStatic
    fun bytes2Bitmap(bytes: ByteArray?, options: BitmapFactory.Options? = null): Bitmap? {
        return if (bytes == null || bytes.isEmpty()) {
            null
        } else {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        }
    }

    // --------------------------------------------------

    fun drawable2Bitmap(drawable: Drawable?, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (drawable != null) {
            var width = reqWidth
            var height = reqHeight
            if (width > 0 && height > 0) {
                if (drawable is PaintDrawable) {
                    drawable.intrinsicWidth = width
                    drawable.intrinsicHeight = height
                } else if (drawable is BitmapDrawable) {
                    // Ensure the bitmap has a density.
                    val bitmap = drawable.bitmap
                    if (bitmap.density == Bitmap.DENSITY_NONE) {
                        drawable.setTargetDensity(DisplayUtil.getMetrics())
                    }
                }
            }
            // 原始大小
            val sourceWidth = drawable.intrinsicWidth
            val sourceHeight = drawable.intrinsicHeight
            if (sourceWidth > 0 && sourceHeight > 0) {
                // 获取原始比例
                val ratio = 1f * sourceWidth / sourceHeight
                // 按照原始比例重新调整图片尺寸
                if (sourceWidth > sourceHeight) {
                    height = (width / ratio).toInt()
                } else if (sourceHeight > sourceWidth) {
                    width = (height * ratio).toInt()
                }
            }
            // --------------------------------------------------
            val config = if (drawable.opacity != PixelFormat.OPAQUE) {
                Bitmap.Config.ARGB_8888
            } else {
                Bitmap.Config.RGB_565
            }
            // 创建指定大小的 Bitmap。
            val bitmap = createBitmap(reqWidth, reqHeight, config)
            // 计算 偏移，保证图片居中
            val left = (reqWidth - width) / 2
            val top = (reqHeight - height) / 2
            val sCanvas = Canvas(bitmap)
            drawable.setBounds(left, top, width + left, height + top)
            drawable.draw(sCanvas)
//            drawable.bounds = Rect(drawable.bounds)
            sCanvas.setBitmap(null)
            return bitmap
        } else {
            return null
        }
    }

    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable?, bmpSize: Int): Bitmap? {
        return drawable2Bitmap(drawable, bmpSize, bmpSize)
    }

    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        return if (drawable != null) {
            drawable2Bitmap(
                drawable,
                drawable.intrinsicWidth.coerceAtLeast(1),
                drawable.intrinsicHeight.coerceAtLeast(1)
            )
        } else {
            null
        }
    }

    @JvmStatic
    fun bitmap2Drawable(res: Resources, bitmap: Bitmap?): Drawable? {
        return bitmap?.toDrawable(res)
    }

// --------------------------------------------------
    /**
     * [drawable] drawable
     * [format] Bitmap.CompressFormat
     * @return ByteArray
     */
    @JvmStatic
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
    @JvmStatic
    fun bytes2Drawable(res: Resources, bytes: ByteArray): Drawable? {
        return bitmap2Drawable(res, bytes2Bitmap(bytes))
    }

    fun getViewBitmap(width: Int, height: Int, view: View?): Bitmap? {
        var bitmap: Bitmap? = null
        if (view != null) {
            val enable = view.isDrawingCacheEnabled
            if (!enable) {
                view.isDrawingCacheEnabled = true
            }
            try {
                if (null != view.drawingCache) {
                    bitmap = ThumbnailUtils.extractThumbnail(view.drawingCache, width, height)
                }
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            } finally {
                if (!enable) {
                    view.isDrawingCacheEnabled = false
                    view.destroyDrawingCache()
                }
            }
        }
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        }
        return bitmap
    }
    // --------------------------------------------------

    @JvmStatic
    fun toRoundRectBitmap(bitmap: Bitmap?, radius: Float): Bitmap? {
        if (bitmap == null) {
            return null
        }
        ZLog.i(ZTag.TAG_DEBUG, "bmp: ${bitmap.width}x${bitmap.height}")
        val bm = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val sCanvas = Canvas(bm)
//        sCanvas.drawARGB(255, 0, 0, 0)
//        sCanvas.drawCircle(radius, radius, radius, paint)
//        sCanvas.drawRoundRect(
//            RectF(0F, 0F, radius, radius),
//            30f,
//            30f,
//            paint
//        )
        paint.color = Color.BLACK
        sCanvas.drawRoundRect(
            RectF(0F, 0F, bitmap.width * 1.0f, bitmap.height * 1.0f),
            radius,
            radius,
            paint
        )
        // 圆画好之后将画笔重置一下
        paint.reset()
        // 设置图像合成模式，该模式为只在源图像和目标图像相交的地方绘制源图像
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        sCanvas.drawBitmap(bitmap, Matrix(), paint)
        return bm
    }

    @JvmStatic
    fun toRoundBitmap(bitmap: Bitmap?, inner: Boolean = false): Bitmap? {
        if (bitmap == null) {
            return null
        }
        return if (inner) {
            innerRound(bitmap)
        } else {
            outRound(bitmap)
        }
    }

    private fun outRound(bitmap: Bitmap): Bitmap {
        val size = hypot(bitmap.width.toDouble(), bitmap.height.toDouble()).toInt()
        val radius = size / 2F
        // 前面同上，绘制图像分别需要bitmap，canvas，paint对象
        val bm = createBitmap(size, size, Bitmap.Config.ARGB_8888)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        ZLog.i(ZTag.TAG, "toRoundBitmap bm : ${bitmap.width}, ${bitmap.height} -> $size")
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.TRANSPARENT
        val canvas = Canvas(bm)
        canvas.drawCircle(radius, radius, radius, paint)
        paint.reset()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, (size - bitmap.width) / 2F, (size - bitmap.height) / 2F, null)
        return bm
    }

    //    private val circlePath = Path()
    private fun innerRound(bitmap: Bitmap): Bitmap {
//        val bm = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        val radius = bitmap.width / 2F
//        val cy = bitmap.height / 2F
//        val cx = bitmap.width / 2F
//        circlePath.reset()
//        circlePath.addCircle(cx, cy, radius, Path.Direction.CW)
//        val canvas = Canvas(bm)
//        canvas.clipPath(circlePath)
//        paint.reset()
//        canvas.drawBitmap(bitmap, 0F, 0F, paint)
//        return bm
        // 前面同上，绘制图像分别需要bitmap，canvas，paint对象
        val bm = createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val radius = bitmap.width / 2F
        val cy = bitmap.height / 2F
        val cx = bitmap.width / 2F
//        ZLog.i(ZTag.TAG, "toRoundBitmap bm : ${bm.width}, ${bm.height} ->${radius * 2} ")
        val canvas = Canvas(bm)
        canvas.drawCircle(cx, cy, radius, paint)
        paint.reset()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0F, 0F, paint)
        return bm
    }
}
