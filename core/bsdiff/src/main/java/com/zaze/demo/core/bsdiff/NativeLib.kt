package com.zaze.demo.core.bsdiff

class NativeLib {

    /**
     * A native method that is implemented by the 'bsdiff' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'bsdiff' library on application startup.
        init {
            System.loadLibrary("bsdiff-android")
        }
    }
}