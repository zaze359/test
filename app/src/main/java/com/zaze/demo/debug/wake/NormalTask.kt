package com.zaze.demo.debug.wake

import android.os.Build
import android.os.SystemClock
import com.zaze.common.base.BaseApplication
import com.zaze.utils.FileUtil
import com.zaze.utils.NetUtil
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.net.HttpURLConnection
import java.net.URL

/**
 * Description :
 * @author : zaze
 * @version : 2020-11-09 - 16:57
 */
open class NormalTask : Runnable {
    private var isRunning = false
    var once = false
    override fun run() {
        isRunning = true
        while (isRunning) {
            doTask()
            if (once) {
                isRunning = false
            } else {
                SystemClock.sleep(5000L)
            }
        }
    }

    open fun doTask() {
        val result = try {
            val url = URL("https://www.baidu.com")
            val urlConn = url.openConnection() as HttpURLConnection
            urlConn.connect()
            val buff = FileUtil.readByBytes(urlConn.inputStream)
//            ZLog.i(ZTag.TAG, "result : $buff")
            buff.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        val log = DateUtil.timeMillisToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " : ${mode()} result >> $result --- ${NetUtil.getNetworkInfo(BaseApplication.getInstance())}\n"
        ZLog.i(ZTag.TAG, "log : $log")
        FileUtil.writeToFile("/sdcard/zaze/wakelock/${Build.MODEL}_${mode()}.txt", log, true)
    }

    open fun release() {
        isRunning = false
    }

    open fun mode(): String {
        return "normal"
    }
}