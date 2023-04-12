package com.zaze.demo.feature.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

object MediaHelper {
    private val mediaMetadataRetriever by lazy {
        MediaMetadataRetriever()
    }

    fun buildEmbeddedPicture(
        context: Context,
        uri: Uri,
        width: Int = -1,
        height: Int = -1
    ): Bitmap? {
        return try {
            mediaMetadataRetriever.setDataSource(context, uri)
            mediaMetadataRetriever.embeddedPicture?.run {
                BitmapFactory.decodeByteArray(this, 0, this.size, buildOptions(width, height))
            }
        } catch (e: Throwable) {
            ZLog.e(ZTag.TAG_DEBUG, "error media : $uri")
            e.printStackTrace()
            null
        }
    }

    fun frameAtTime(context: Context, uri: Uri): Bitmap? {
        return try {
            mediaMetadataRetriever.setDataSource(context, uri)
            mediaMetadataRetriever.frameAtTime
        } catch (e: Throwable) {
            ZLog.e(ZTag.TAG_DEBUG, "error media : $uri")
            e.printStackTrace()
            null
        }
    }

    fun buildOptions(width: Int, height: Int): BitmapFactory.Options {
        return BitmapFactory.Options().apply {
            inJustDecodeBounds = false
            inPreferredConfig = Bitmap.Config.RGB_565
            if (width > 0 && height > 0) {
                if (outWidth == 0 || outHeight == 0) {
                    outWidth = width
                    outHeight = height
                } else {
                    inSampleSize = if (outWidth >= outHeight) {
                        outWidth / width
                    } else {
                        outHeight / height
                    }
                }
            }
        }
    }


}