package com.zaze.demo.debug.wake

import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock

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

    override fun run() {
        super.run()
    }

    override fun release() {
        super.release()
        wakeLock?.release()

    }
}