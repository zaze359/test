package com.zaze.aarrepo.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Description	: 任务管理
 *
 * @author : zaze
 * @version : 2015年6月9日 上午10:12:28
 */
public class ThreadManager {
    /**
     * 后台任务
     */
    private final ExecutorService backgroundExecutor;
    /**
     * 单线程
     */
    private final ExecutorService singleExecutor;
    /**
     * 多线程
     */
    private ExecutorService multiExecutor;
    /**
     * 并发数
     */
    private int maxThreadNum = 5;
    private Handler handler;

    private static volatile ThreadManager instance;

    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    private ThreadManager() {
        super();
        backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "background executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
        //
        singleExecutor = Executors.newSingleThreadExecutor();
        multiExecutor = Executors.newFixedThreadPool(maxThreadNum);
        //
        handler = new Handler(Looper.getMainLooper());
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
        multiExecutor = Executors.newFixedThreadPool(maxThreadNum);
    }

    /**
     * Description	: 在后台中执行(守卫进程)
     *
     * @param runnable
     * @author : zaze
     * @version : 2015年6月9日 上午10:12:07
     */
    public void runInBackgroundThread(final Runnable runnable) {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Description	: 单线程执行
     *
     * @param runnable
     * @author : zaze
     * @version : 2015年6月9日 上午10:12:07
     */
    public void runInSingleThread(final Runnable runnable) {
        singleExecutor.execute(runnable);
    }

    /**
     * Description	: 多线程执行
     *
     * @param runnable
     * @author : zaze
     * @version : 2015年6月9日 上午10:12:07
     */
    public void runInMultiThread(Runnable runnable) {
        multiExecutor.execute(runnable);
    }

    /**
     * Description	: 多线程执行
     *
     * @param runnable
     * @author : zaze
     * @version : 2015年6月9日 上午10:12:07
     */
    public void runInUIThread(Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * Description	: 多线程执行
     *
     * @param runnable
     * @author : zaze
     * @version : 2015年6月9日 上午10:12:07
     */
    public void runInUIThread(Runnable runnable, int delay) {
        handler.postDelayed(runnable, delay);
    }

    /**
     * Description	: 终止单线程调用中的方法 等待当前的线程执行完毕后终止所有的线程
     *
     * @author : zaze
     * @version : 2015年6月9日 下午12:31:54
     */
    public void shutdownSingleThread() {
        singleExecutor.shutdown();
    }

    /**
     * Description	: 终止多线程  等待当前的线程执行完毕后终止所有的线程
     *
     * @author : zaze
     * @version : 2015年6月9日 下午12:31:54
     */
    public void shutdownMultiThread() {
        multiExecutor.shutdown();
    }

    /**
     * Description	: 终止后台  等待当前的线程执行完毕后终止所有的线程
     *
     * @author : zaze
     * @version : 2015年6月9日 下午12:31:54
     */
    public void shutdownBackgroundThread() {
        backgroundExecutor.shutdown();
    }
}
