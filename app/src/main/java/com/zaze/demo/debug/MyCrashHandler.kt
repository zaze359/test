package com.zaze.demo.debug

import android.os.Process
import android.util.Log
import java.lang.Thread.UncaughtExceptionHandler
import kotlin.system.exitProcess

class MyCrashHandler(private val defaultHandler: UncaughtExceptionHandler?) : UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        dump(t, e)
        defaultHandler?.uncaughtException(t, e) ?: let {
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
    }

    private fun dump(t: Thread, e: Throwable) {
        Log.i("MyCrashHandler", "dump: $e")
    }
}