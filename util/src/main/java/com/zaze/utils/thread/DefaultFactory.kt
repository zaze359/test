package com.zaze.utils.thread

import android.util.Log
import java.util.concurrent.ThreadFactory

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 23:34
 */
class DefaultFactory(val name: String? = "DefaultFactory") : ThreadFactory {
    private var group: ThreadGroup = System.getSecurityManager()?.threadGroup
            ?: Thread.currentThread().threadGroup

    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(group, r, name, 0)
        if (thread.isDaemon) {   //设为非后台线程
            thread.isDaemon = false
        }
        if (thread.priority != Thread.NORM_PRIORITY) { //优先级为normal
            thread.priority = Thread.NORM_PRIORITY
        }
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { th, ex ->
            Log.i("ThreadFactory", "Appeared exception! Thread [" + th.name + "], because [" + ex.message + "]")
        }
        return thread
    }

}

