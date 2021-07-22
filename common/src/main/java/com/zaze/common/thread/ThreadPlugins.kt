package com.zaze.common.thread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import io.reactivex.Scheduler
import kotlinx.coroutines.Runnable
import java.util.concurrent.*

/**
 * Description :
 * 1. corePoolSize : 保留线程数;
 * 2. 仅当超过了队列上限时, 才会开辟新线程
 * @author : ZAZE
 * @version : 2018-12-19 - 21:04
 */
object ThreadPlugins {
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    // --------------------------------------------------
    val requestExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadExecutorStub(
            ThreadPoolExecutor(
                0,
                CPU_COUNT * 2,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                DefaultFactory("zRequest")
            )
        )
    }
    // --------------------------------------------------
    /**
     * 普通子线程池，用于一般子线程任务操作
     * 耗时短
     */
    val multiExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadExecutorStub(
            ThreadPoolExecutor(
                0,
                CPU_COUNT * 2,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                DefaultFactory("zMulti")
            )
        )
    }
    // --------------------------------------------------
    /**
     * file操作线程
     */
    val fileExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadExecutorStub(
            ThreadPoolExecutor(
                1,
                1,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                DefaultFactory("zFile")
            )
        )
    }

    // --------------------------------------------------
    val dbExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadExecutorStub(
            ThreadPoolExecutor(
                1,
                1,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                DefaultFactory("zIO")
            )
        )
    }
    // --------------------------------------------------
    /**
     * ui thread
     */
    private val uiHandler = Handler(Looper.getMainLooper())

    // --------------------------------------------------

    /**
     * 自定义工作线程
     */
    @JvmStatic
    @Volatile
    private var workThread = HandlerThread("z_work_thread").apply { start() }

    /**
     * 自定义Handler
     */
    @JvmStatic
    @Volatile
    private var workHandler = Handler(workThread.looper)

    // --------------------------------------------------
    /**
     * 在io线程执行
     * [runnable] runnable
     */
    @JvmStatic
    fun runInIoThread(runnable: Runnable) {
        dbExecutorStub.execute(runnable)
    }

    /**
     * 获取用于 RxJava 的 io调度器
     * @return 用于 RxJava 的 io调度器
     */
    @JvmStatic
    fun ioScheduler(): Scheduler {
        return dbExecutorStub.rxScheduler
    }

    // --------------------------------------------------
    /**
     * UI线程执行
     *
     * @param runnable runnable
     */
    @JvmStatic
    fun runInUIThread(runnable: Runnable, delay: Long = 0) {
        uiHandler.removeCallbacks(runnable)
        uiHandler.postDelayed(runnable, delay)
    }
    // --------------------------------------------------
    /**
     * 在自定义工作线程中移除消息
     */
    @JvmStatic
    fun removeWorkMessages(what: Int) {
        workHandler.removeMessages(what)
    }

    /**
     * 在自定义工作线程中发送消息
     */
    @JvmStatic
    fun sendWorkMessage(what: Int, delay: Long = 0) {
        removeWorkMessages(what)
        workHandler.sendEmptyMessageDelayed(what, delay)
    }

    /**
     * 在自定义工作线程中执行延迟操作
     */
    @JvmStatic
    fun runInWorkThread(runnable: Runnable, delay: Long = 0) {
        workHandler.removeCallbacks(runnable)
        workHandler.postDelayed(runnable, delay)
    }

    @JvmStatic
    fun removeWorkCallbacks(runnable: Runnable) {
        workHandler.removeCallbacks(runnable)
    }

    // --------------------------------------------------
    @JvmStatic
    fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }
}