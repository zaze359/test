package com.zaze.demo.debug

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader


private fun test() {
    val a = A()
    val result = a.a("b")
    println(result?.length)
    val b = B()
    b.b()
}