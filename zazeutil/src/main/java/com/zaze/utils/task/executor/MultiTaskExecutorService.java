package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.ZCallback;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description : 多任务池服务
 *
 * @author zaze
 * @version 2017/3/25 - 上午12:25 1.0
 */
class MultiTaskExecutorService extends FilterTaskExecutorService {
    private static ThreadPoolExecutor multiExecutor;

    public MultiTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
        multiExecutor = new ThreadPoolExecutor(5, 5 * Runtime.getRuntime().availableProcessors(), 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "MultiTaskExecutorService");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
        multiExecutor.allowCoreThreadTimeOut(true);
    }

    /**
     * 执行一批多个任务(最大同时执行上限10)
     *
     * @param num 执行任务数
     */
    public void multiExecuteTask(int num, final ZCallback<Boolean> callback) {
        for (int i = 0; i < num; i++) {
            multiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    boolean b = executeNextTask();
                    if (callback != null) {
                        callback.onNext(b);
                    }
                }
            });
        }
    }
}
