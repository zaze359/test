package com.zaze.core.nativelib

import android.graphics.Bitmap

class MyJpegCompressor {
    external fun compress(bitmap: Bitmap, quality: Int, outPath: String, optimize: Boolean): Int
}