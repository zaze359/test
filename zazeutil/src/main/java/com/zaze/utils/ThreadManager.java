package com.zaze.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description	: 任务管理
 *
 * @author : zaze
 * @version : 2015年6月9日 上午10:12:28
 */
public class ThreadManager {
    private static boolean needLog = false;
    /**
     * 后台任务
     */
    private ThreadPoolExecutor backgroundExecutor;

    /**
     * 推荐磁盘读写调用的线程池
     */
    private ThreadPoolExecutor diskIO;

    /**
     * 单线程
     */
    private ThreadPoolExecutor singleExecutor;

    /**
     * 多线程
     */
    private ThreadPoolExecutor multiExecutor;

    private MainThreadExecutor mainThread;

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
        initDiskIO();
        initBackgroundExecutor();
        initSingleExecutor();
        initMultiExecutor();
        // --------------------------------------------------
        mainThread = new MainThreadExecutor();
    }
    // --------------------------------------------------

    private void initDiskIO() {
        diskIO = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r, "diskIO");
                        if (thread.isDaemon()) {
                            thread.setDaemon(false);
                        }
                        if (needLog) {
                            ZLog.i(ZTag.TAG_DEBUG, "run %s(%s)", thread.getName(), thread.getId());
                        }
                        return thread;
                    }
                });
    }

    private void initSingleExecutor() {
        singleExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r, "singleExecutor");
                        if (thread.isDaemon()) {
                            thread.setDaemon(false);
                        }
                        if (needLog) {
                            ZLog.i(ZTag.TAG_DEBUG, "run %s(%s)", thread.getName(), thread.getId());
                        }
                        return thread;
                    }
                });
    }

    private void initMultiExecutor() {
//        maxThreadNum * Runtime.getRuntime().availableProcessors()
        multiExecutor = new ThreadPoolExecutor(3, 10, 30L, TimeUnit.SECONDS
                , new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "multiExecutor");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                if (needLog) {
                    ZLog.i(ZTag.TAG_DEBUG, "run %s(%s)", thread.getName(), thread.getId());
                }
                return thread;
            }
        });
        multiExecutor.allowCoreThreadTimeOut(true);
    }

    private void initBackgroundExecutor() {
        backgroundExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable, "backgroundExecutor");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                if (needLog) {
                    ZLog.i(ZTag.TAG_DEBUG, "run %s(%s)", thread.getName(), thread.getId());
                }
                return thread;
            }
        });
    }
    // --------------------------------------------------

    /**
     * Description	: 在后台中执行(守卫进程)
     *
     * @param runnable runnable
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

    public void runInDiskIO(Runnable runnable) {
        diskIO.execute(runnable);
    }


    /**
     * Description	: 单线程执行
     *
     * @param runnable runnable
     */
    public void runInSingleThread(Runnable runnable) {
        singleExecutor.execute(runnable);
    }

    /**
     * Description	: 多线程执行
     *
     * @param runnable runnable
     */
    public void runInMultiThread(Runnable runnable) {
        multiExecutor.execute(runnable);
    }

    /**
     * Description	: UI线程执行
     *
     * @param runnable runnable
     */
    public void runInUIThread(Runnable runnable) {
        mainThread.execute(runnable);
    }

    /**
     * Description	: UI线程执行
     *
     * @param runnable runnable
     */
    public void runInUIThread(Runnable runnable, long delay) {
        mainThread.executeDelayed(runnable, delay);
    }

    /**
     * Description	: 终止单线程调用中的方法 等待当前的线程执行完毕后终止所有的线程
     */
    public void shutdownSingleThread() {
        if (needLog) {
            ZLog.i(ZTag.TAG_DEBUG, "shutdownSingleThread");
        }
        singleExecutor.shutdown();
        initSingleExecutor();
    }

    /**
     * Description	: 终止多线程  等待当前的线程执行完毕后终止所有的线程
     */
    public void shutdownMultiThread() {
        if (needLog) {
            ZLog.i(ZTag.TAG_DEBUG, "shutdownMultiThread");
        }
        multiExecutor.shutdown();
        initMultiExecutor();
    }

    /**
     * Description	: 终止后台  等待当前的线程执行完毕后终止所有的线程
     */
    public void shutdownBackgroundThread() {
        if (needLog) {
            ZLog.i(ZTag.TAG_DEBUG, "shutdownBackgroundThread");
        }
        backgroundExecutor.shutdown();
        initBackgroundExecutor();
    }

    // --------------------------------------------------

    /**
     * 是否需要log
     *
     * @param needLog
     */
    public static void setNeedLog(boolean needLog) {
        ThreadManager.needLog = needLog;
    }

    // --------------------------------------------------

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }

        public void executeDelayed(@NonNull Runnable command, long delayMillis) {
            mainThreadHandler.postDelayed(command, delayMillis);
        }
    }
}
