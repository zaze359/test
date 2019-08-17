package com.zaze.demo.debug

import android.util.Log
import java.util.concurrent.CountDownLatch

/**
 * Description : 启动任务
 *
 * @author : ZAZE
 */
abstract class StartupTask(private val taskName: String? = null) : Runnable {

    private var isStartup = false
    var countDownLatch: CountDownLatch? = null
    var debugLog = false

    override fun run() {
        val startTime = System.currentTimeMillis()
        isStartup = try {
            doTask()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            countDownLatch?.countDown()
        }
        if (debugLog) {
            val msg = "${(taskName
                    ?: this.hashCode().toString())} >> Startup : $isStartup >> ${System.currentTimeMillis() - startTime}ms"
            if (isStartup) {
                Log.i("StartupTask", msg)
            } else {
                Log.w("StartupTask", msg)
            }
        }
    }

    abstract fun doTask()
}