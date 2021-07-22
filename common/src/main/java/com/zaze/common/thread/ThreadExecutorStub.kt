package com.zaze.common.thread

import com.zaze.common.BuildConfig
import com.zaze.utils.log.ZLog
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import kotlin.coroutines.CoroutineContext

/**
 * Description :
 * @author : zaze
 * @version : 2021-06-26 - 11:07
 */
class ThreadExecutorStub(val executorService: ThreadPoolExecutor) {
    /**
     * 用于 RxJava 的 网络请求调度器
     */
    val rxScheduler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Schedulers.from(executorService)
    }

    val coroutineDispatcher by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        object : ExecutorCoroutineDispatcher() {
            override val executor: Executor
                get() = executorService

            override fun close() {
            }

            override fun toString(): String = executorService.toString()

            override fun dispatch(context: CoroutineContext, block: Runnable) {
                executorService.execute(block)
            }
        }
    }

    fun printThreadInfo(tag: String) {
        if (BuildConfig.DEBUG) {
            // only used by debug start
            val traces = Thread.currentThread().stackTrace
            val builder = StringBuilder()
            builder.append(
                String.format(
                    "thread id %s >> group %s\n",
                    Thread.currentThread().name,
                    Thread.currentThread().threadGroup?.name
                )
            )
            traces.forEachIndexed { index, stackTraceElement ->
                builder.append(stackTraceElement.className)
                    .append(".")
                    .append(stackTraceElement.methodName)
                    .append(":")
                    .append("" + stackTraceElement.lineNumber).append("\n");
            }
            ZLog.w(tag, builder.toString());
        }
    }

    fun remove(runnable: Runnable) {
        executorService.remove(runnable)
    }

    fun execute(runnable: Runnable) {
        remove(runnable)
        executorService.execute(runnable)
    }

    fun isOnThread(): Boolean {
        return when (val threadFactory = executorService.threadFactory) {
            is DefaultFactory -> {
                Thread.currentThread().threadGroup?.name == threadFactory.name
            }
            else -> {
                false
            }
        }
    }
}