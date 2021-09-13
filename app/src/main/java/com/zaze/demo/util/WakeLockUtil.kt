package com.zaze.demo.util

import android.content.Context
import android.os.PowerManager

/**
 * Description :
 * @author : zaze
 * @version : 2021-06-21 - 09:50
 */
object WakeLockUtil {

    @JvmStatic
    fun newScreenWakeLock(context: Context): PowerManager.WakeLock? {
        return newWakeLock(
            context,
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK
        )
    }

    @JvmStatic
    fun newPartialWakeLock(context: Context): PowerManager.WakeLock? {
        return newWakeLock(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    @JvmStatic
    fun newWakeLock(context: Context, levelAndFlags: Int): PowerManager.WakeLock? {
        return (context.getSystemService(Context.POWER_SERVICE) as PowerManager?)?.newWakeLock(
            levelAndFlags,
            this.javaClass.name
        )
    }

    @JvmStatic
    fun acquire(wakeLock: PowerManager.WakeLock?, timeout: Long = 3_000L) {
        try {
            wakeLock?.acquire(timeout)
        } catch (e: Exception) {
        }
    }

    @JvmStatic
    fun release(wakeLock: PowerManager.WakeLock?) {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock.release()
            }
        } catch (e: Exception) {
        }
    }
}