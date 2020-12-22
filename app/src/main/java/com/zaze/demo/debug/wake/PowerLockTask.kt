package com.zaze.demo.debug.wake

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import com.zaze.demo.debug.HttpDownloader
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.net.HttpURLConnection
import java.net.URL

/**
 * Description :
 * @author : zaze
 * @version : 2020-11-09 - 17:00
 */
class PowerLockTask(context: Context) : NormalTask() {
    private var wakeLock: WakeLock? = null

    init {
        // --------------------------------------------------
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.javaClass.name)
        wakeLock?.acquire()
    }

    override fun mode(): String {
        return "PowerLock"
    }

    override fun release() {
        super.release()
        wakeLock?.release()
    }


}