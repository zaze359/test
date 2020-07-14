package com.zaze.demo.debug

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.FileLock

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-05-12 - 13:25
 */
class KeepLiveService : Service() {
    private var thread = LockThread()
    private var lockFile: File? = null
    private var keepApp: Pair<String, String>? = null


    companion object {
        const val PACKAGE_NAME = "packageName"
        const val LOCK_FILE = "lockFile"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(this.packageName, "KeepLiveService : onCreate")
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
        keepApp = Pair(appPackage, lockFile)
        Log.i(this.packageName, "KeepLiveService : onStartCommand >> $thread")
        Log.i(this.packageName, "KeepLiveService : ${thread.state}")
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
        Log.i(this.packageName, "KeepLiveService : onDestroy")
        super.onDestroy()
        thread.interrupt()
    }

    inner class LockThread : Thread() {
        override fun run() {
            super.run()
            keepApp?.let {
                Log.i("plock", "plock ${it.first} >> ${it.second} start")
//                lock(File(it.second))
                Log.i("plock", "plock ${it.first} >> ${it.second}  end")
            }
        }

        private fun lock(lockFile: File): FileLock? {
            try {
                val fos = FileOutputStream(lockFile)
                val fl = fos.channel.lock()
                if (fl.isValid) {
                    ZLog.i(ZTag.TAG, "LockThread lock " + lockFile.absolutePath + " SUC!")
                    return fl
                }
            } catch (e: Exception) {
                ZLog.w(ZTag.TAG, "LockThread lock " + lockFile.absolutePath + " FAIL!", e)
            }
            return null
        }
    }
}