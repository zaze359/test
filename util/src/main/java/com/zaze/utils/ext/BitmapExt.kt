package com.zaze.utils.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object BitmapExt {

    /**
     * 解码图片后，进行尺寸压缩
     */
    fun decodeToBitmap(
        width: Int,
        height: Int,
        onDecode: (BitmapFactory.Options?) -> Bitmap
    ): Bitmap {
        if (width <= 0 || height <= 0) { // 不缩放，直接解码
            return onDecode(null)
        }
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        // 第一次 decode，获取宽高。
        onDecode(options)
        options.apply {
            inJustDecodeBounds = false
//            ZLog.i(ZTag.TAG, "outWidth:$outWidth, outHeight:$outHeight")
            if (outWidth == 0 || outHeight == 0) {
                outWidth = width
                outHeight = height
            } else {
                inSampleSize = 1
                // 仅当 宽高 都比 需求的尺寸大时 才缩放
                // 保证 inSampleSize 的取值是 2的幂次
                // 防止缩太小 需要进行拉伸。
                var (tempWidth: Int, tempHeight: Int) = outWidth to outHeight
                while (tempWidth > width && tempHeight > height) {
                    inSampleSize *= 2
                    tempWidth /= 2
                    tempHeight /= 2
                }
                ZLog.i(ZTag.TAG, "inSampleSize:$inSampleSize")
            }
        }
        return onDecode(options)
    }
}

/**
 * bitmap2Bytes
 */
fun Bitmap?.toBytes(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray? {
    return this?.let {
        val outputStream = ByteArrayOutputStream()
        it.compress(format, 100, outputStream)
        outputStream.toByteArray()
    }
}

/**
 * 将 bitmap 压缩到指定大小
 * [limitSize] 限制大小, 单位 byte。
 */
fun Bitmap.compressToLimit(
    format: Bitmap.CompressFormat,
    stream: OutputStream,
    limitSize: Long
) {
    ZLog.i(
        ZTag.TAG,
        "width:${this.width}; height: ${this.height}; byteCount: $byteCount; limitSize: $limitSize"
    )
    if (limitSize <= 0) {
        return
    }
    var quality = 60
    val tempStream = ByteArrayOutputStream()
    try {
        do {
            tempStream.reset()
            this.compress(format, quality, tempStream)
            quality -= 10
        } while (tempStream.size() > limitSize && quality > 0)
        stream.write(tempStream.toByteArray())
    } catch (e: Throwable) {
        e.printStackTrace()
    } finally {
        stream.flush()
        stream.close()
    }
}


fun Bitmap.saveToFile(
    outFile: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    limitSize: Long = -1
) {
    var outputStream: OutputStream? = null
    try {
        outputStream = FileOutputStream(outFile)
        compressToLimit(format, outputStream, limitSize)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            outputStream?.flush()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}