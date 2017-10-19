package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.ExecutorService;
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
class AsyncTaskExecutorService extends FilterTaskExecutorService {
    protected ExecutorService executorService;

    public AsyncTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
        executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "AsyncTaskExecutorService");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
    }

    /**
     * 异步执行任务
     */
    public boolean executeAsyncTask() {
        if (taskExecutorService == null) {
            return false;
        }
        if (!taskExecutorService.isEmpty()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    running();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    protected void running() {
        if (needLog) {
            ZLog.d(ZTag.TAG_TASK, "running");
        }
        taskExecutorService.executeNextTask();
    }
}
