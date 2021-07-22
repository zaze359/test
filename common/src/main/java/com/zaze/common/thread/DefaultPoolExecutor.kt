package com.zaze.common.thread

import android.util.Log
import java.util.concurrent.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 23:11
 */
class DefaultPoolExecutor private constructor(
    corePoolSize: Int,
    maximumPoolSize: Int,
    keepAliveTime: Long,
    unit: TimeUnit,
    workQueue: BlockingQueue<Runnable>,
    threadFactory: ThreadFactory
) : ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    unit,
    workQueue,
    threadFactory,
    RejectedExecutionHandler { _, executor ->
        Log.e("DefaultPoolExecutor", "Task rejected, too many task! $executor")
    }) {

    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val INIT_THREAD_COUNT = CPU_COUNT + 1
        private val MAX_THREAD_COUNT = INIT_THREAD_COUNT
        private const val SURPLUS_THREAD_LIFE = 30L

        private var INSTANCE: DefaultPoolExecutor? = null

        @JvmStatic
        fun getInstance(): DefaultPoolExecutor {
            Log.i("CPU_COUNT", "CPU_COUNT: $CPU_COUNT")
            return INSTANCE ?: DefaultPoolExecutor(
                INIT_THREAD_COUNT,
                MAX_THREAD_COUNT,
                SURPLUS_THREAD_LIFE,
                TimeUnit.SECONDS,
                ArrayBlockingQueue(64),
                DefaultFactory()
            ).also {
                INSTANCE = it
            }
        }
    }

}