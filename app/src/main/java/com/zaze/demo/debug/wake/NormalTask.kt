package com.zaze.demo.debug.wake

import android.os.Build
import android.os.SystemClock
import com.zaze.common.base.BaseApplication
import com.zaze.utils.FileUtil
import com.zaze.utils.NetUtil
import com.zaze.utils.date.DateUtil

/**
 * Description :
 * @author : zaze
 * @version : 2020-11-09 - 16:57
 */
open class NormalTask : Runnable {
    private var isRunning = false

    override fun run() {
        isRunning = true
        while (isRunning) {
            doTask()
            SystemClock.sleep(5000L)
        }
    }

    open fun doTask() {
        FileUtil.writeToFile("/sdcard/zaze/wakelock/${Build.MODEL}_normal.txt", DateUtil.timeMillisToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss : ") + "${NetUtil.getNetworkInfo(BaseApplication.getInstance())}\n", true)
    }

    open fun release() {
        isRunning = false
    }
}