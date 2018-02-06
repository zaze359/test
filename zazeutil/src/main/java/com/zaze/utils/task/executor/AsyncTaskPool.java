package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.TaskEmitter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description  : 异步任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
public class AsyncTaskPool extends FilterTaskPool {

    public static AsyncTaskPool newInstance(TaskPool taskPool) {
        return new AsyncTaskPool(taskPool);
    }

    private AsyncTaskPool(TaskPool taskPool) {
        super(taskPool);
        executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "AsyncTaskPool");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
    }

    @Override
    public boolean executeTask(@NonNull final TaskEmitter emitter) {
        if (!isEmpty()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (needLog) {
                        ZLog.i(ZTag.TAG_TASK, "AsyncTaskPool");
                    }
                    if (!isStop) {
                        AsyncTaskPool.super.executeTask(emitter);
                    }
                }
            });
            return true;
        } else {
            return false;
        }
    }
}
