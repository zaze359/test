package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
public class AutoTaskPool extends FilterTaskPool {
    private boolean isRunning = false;

    public static AutoTaskPool newInstance(TaskPool taskPool) {
        return new AutoTaskPool(taskPool);
    }

    private AutoTaskPool(TaskPool taskPool) {
        super(taskPool);
        executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "AutoTaskPool");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
    }

    @Override
    public boolean executeTask() {
        if (!isRunning) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (needLog) {
                        ZLog.i(ZTag.TAG_TASK, "AutoTaskPool start");
                    }
                    isRunning = true;
                    while (!taskPool.isEmpty()) {
                        AutoTaskPool.super.executeTask();
                    }
                    isRunning = false;
                    if (needLog) {
                        ZLog.d(ZTag.TAG_TASK, "AutoTaskPool end");
                    }
                }
            });
        }
        return true;

    }
}
