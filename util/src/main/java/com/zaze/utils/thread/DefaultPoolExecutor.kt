package com.zaze.utils.thread

import android.util.Log
import java.util.concurrent.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 23:11
 */
class DefaultPoolExecutor : ThreadPoolExecutor {

    companion object {
        private const val INIT_THREAD_COUNT = 1
        private const val SURPLUS_THREAD_LIFE = 30L
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val MAX_THREAD_COUNT = CPU_COUNT + 1


        private var INSTANCE: DefaultPoolExecutor? = null

        @JvmStatic
        fun getInstance(): DefaultPoolExecutor {
            return INSTANCE ?: DefaultPoolExecutor(INIT_THREAD_COUNT,
                    MAX_THREAD_COUNT,
                    SURPLUS_THREAD_LIFE,
                    TimeUnit.SECONDS,
                    ArrayBlockingQueue(2),
                    DefaultFactory()
            ).also {
                INSTANCE = it
            }
        }
    }

    private constructor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit, workQueue: BlockingQueue<Runnable>, threadFactory: ThreadFactory)
            : super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, RejectedExecutionHandler { r, executor ->
        Log.e("DefaultPoolExecutor", "Task rejected, too many task!")
        throw RejectedExecutionException("Task $r rejected from $executor")
    })
}