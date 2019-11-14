package com.zaze.common.thread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.zaze.utils.thread.DefaultFactory
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Description :
 * 1. corePoolSize : 保留线程数;
 * 2. 仅当超过了队列上限时, 才会开辟新线程
 * @author : ZAZE
 * @version : 2018-12-19 - 21:04
 */
object ThreadPlugins {

    /**
     * cup 数量
     */
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    /**
     * 网络请求线程池
     */
    private val requestExecutor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadPoolExecutor(1, Math.min(CPU_COUNT, 2), 1L, TimeUnit.MINUTES, LinkedBlockingQueue(), DefaultFactory("request"))
    }

    /**
     * 用于 RxJava 的 网络请求调度器
     */
    private val requestScheduler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Schedulers.from(requestExecutor)
    }

    // --------------------------------------------------

    /**
     * io操作线程
     */
    private val ioExecutor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES, LinkedBlockingQueue(), DefaultFactory("io"))
    }

    /**
     * 用于 RxJava 的 io 调度器
     */
    private val ioScheduler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Schedulers.from(ioExecutor)
    }

    // --------------------------------------------------
    /**
     * 处理下载的回调操作
     */
    private val downloadExecutor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES, LinkedBlockingQueue(), DefaultFactory("download"))
    }

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
    private var workThread = HandlerThread("work_thread").apply { start() }

    /**
     * 自定义Handler
     */
    @JvmStatic
    @Volatile
    private var workHandler = Handler(workThread.looper)

    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * 在下载线程执行
     * [runnable] runnable
     */
    @JvmStatic
    fun runInRequestThread(runnable: Runnable) {
        requestExecutor.execute(runnable)
    }
    /**
     * 获取用于 RxJava 的 网络请求调度器
     * @return 用于 RxJava 的 网络请求调度器
     */
    @JvmStatic
    fun requestScheduler(): Scheduler {
        return requestScheduler
    }

    // --------------------------------------------------
    /**
     * 在下载线程执行
     * [runnable] runnable
     */
    @JvmStatic
    fun runInDownloadThread(runnable: Runnable) {
        downloadExecutor.execute(runnable)
    }

    // --------------------------------------------------

    /**
     * 在io线程执行
     * [runnable] runnable
     */
    @JvmStatic
    fun runInIoThread(runnable: Runnable) {
        ioExecutor.execute(runnable)
    }

    /**
     * 获取用于 RxJava 的 io调度器
     * @return 用于 RxJava 的 io调度器
     */
    @JvmStatic
    fun ioScheduler(): Scheduler {
        return ioScheduler
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
}