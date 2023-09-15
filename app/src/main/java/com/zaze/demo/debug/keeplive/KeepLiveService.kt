package com.zaze.demo.debug.keeplive

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.FileLock

/**
 * Description : 通过文件锁  FileLock 来做进程保活
 *
 * @author : ZAZE
 * @version : 2020-05-12 - 13:25
 */
class KeepLiveService : Service() {
    private var thread = LockThread()

    //    private var lockFile: File? = null
    private var keepAppMeta: Pair<String, String>? = null

    companion object {
        private val TAG = "KeepLiveService"

        const val PACKAGE_NAME = "packageName"
        const val LOCK_FILE = "lockFile"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startCompatibility = super.onStartCommand(intent, flags, startId)
        if (intent == null) {
            return startCompatibility
        }
        val appPackage = intent.getStringExtra(PACKAGE_NAME)
        val lockFile = intent.getStringExtra(LOCK_FILE)
        if (appPackage.isNullOrEmpty() || lockFile.isNullOrEmpty()) {
            return startCompatibility
        }
        keepAppMeta = Pair(appPackage, lockFile)
        Log.i(TAG, "onStartCommand thread.state >> ${thread.state}")
        when (thread.state) {
            Thread.State.RUNNABLE -> {
                return startCompatibility
            }

            Thread.State.NEW -> {
            }

            Thread.State.TERMINATED -> {
                thread = LockThread()
            }

            else -> {
                return startCompatibility
            }
        }
        if (thread.isInterrupted) {
            thread = LockThread()
        }
        thread.start()
        return startCompatibility
    }

    override fun onDestroy() {
        Log.i(TAG, " onDestroy")
        super.onDestroy()
        thread.interrupt()
    }

    inner class LockThread : Thread() {
        override fun run() {
            super.run()
            keepAppMeta?.let {
                val packageName = it.first
                val lockFile = it.second
                Log.i(TAG, "plock $packageName >> $lockFile start")
                val lock = lock(File(lockFile))
                Log.i(TAG, "plock $packageName >> $lockFile  end")
            }
        }

        private fun lock(lockFile: File): FileLock? {
            try {
                val fos = FileOutputStream(lockFile)
                val fl = fos.channel.lock()
                if (fl.isValid) {
                    Log.i(TAG, "LockThread lock " + lockFile.absolutePath + " SUC!")
                    return fl
                }
            } catch (e: Exception) {
                Log.w(TAG, "LockThread lock " + lockFile.absolutePath + " FAIL!", e)
            }
            return null
        }
    }
}