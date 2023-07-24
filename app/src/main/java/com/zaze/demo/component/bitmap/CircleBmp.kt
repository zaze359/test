package com.zaze.demo.component.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader


private val circlePath = Path()
fun innerRound(bitmap: Bitmap): Bitmap {
    val radius = bitmap.width / 2F
    val cy = bitmap.height / 2F
    val cx = bitmap.width / 2F
    circlePath.reset()
    circlePath.addCircle(cx, cy, radius, Path.Direction.CW)

    val bm = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    canvas.clipPath(circlePath)
    val paint = Paint()
    paint.reset()
    canvas.drawBitmap(bitmap, 0F, 0F, paint)
    return bm
}


fun innerRound2(bitmap: Bitmap): Bitmap {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.reset()
    paint.shader = BitmapShader(
        bitmap, Shader.TileMode.CLAMP,
        Shader.TileMode.CLAMP
    )
    val bm = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    val radius = bitmap.width / 2F
    val cy = bitmap.height / 2F
    val cx = bitmap.width / 2F
    canvas.drawCircle(cx, cy, radius, paint)
    return bm
}


fun scale(sourceBitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
    val width: Int = sourceBitmap.width
    val height: Int = sourceBitmap.height

    val widthScale = dstWidth.toFloat() / width
    val heightScale = dstHeight.toFloat() / height

    val scale: Float = widthScale.coerceAtLeast(heightScale)
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    // 使用 matrix 缩放
    return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true)
}