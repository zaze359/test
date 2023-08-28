package com.zaze.core.network

import android.util.Log
import java.util.concurrent.ThreadFactory

class NetworkThreadFactory : ThreadFactory {
    companion object {
        private const val name = "NetworkThreadFactory"
    }

    private val group = ThreadGroup(name)
    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(group, r, name, 0)
        if (thread.isDaemon) {   //设为非后台线程
            thread.isDaemon = false
        }
        if (thread.priority != Thread.NORM_PRIORITY) { //优先级为normal
            thread.priority = Thread.NORM_PRIORITY
        }
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { th, ex ->
            Log.i(
                "ThreadFactory",
                "Appeared exception! Thread [" + th.name + "], because [" + ex.message + "]"
            )
            throw ex
        }
        return thread
    }

}
