package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.MultiNum;
import com.zaze.utils.task.TaskEmitter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Description : 多任务池服务,
 *
 * @author zaze
 * @version 2017/3/25 - 上午12:25 1.0
 */
public class MultiTaskPool extends FilterTaskPool {
    private static final int MIN = 1;
    private static final int DEFAULT = 3;
    private static final int MAX = 6;
    private static final long KEEP_ALIVE_TIME = 60L;

    private MyThreadPoolExecutor multiExecutor;
    private volatile @MultiNum
    int multiNum = DEFAULT;

    public static MultiTaskPool newInstance(TaskPool taskPool) {
        return taskPool == null ? null : new MultiTaskPool(taskPool);
    }

    private MultiTaskPool(TaskPool taskPool) {
        super(taskPool);
        multiExecutor = new MyThreadPoolExecutor(MIN, MAX, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "MultiTaskPool");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
        multiExecutor.allowCoreThreadTimeOut(true);
    }

    /**
     * 执行一批多个任务(最大同时执行上限5)
     */
    @Override
    public boolean executeTask(final TaskEmitter emitter) {
        for (int i = 0; i < multiNum; i++) {
            multiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isStop) {
                        if (needLog) {
                            ZLog.i(ZTag.TAG_TASK, "MultiTaskPool");
                        }
                        MultiTaskPool.super.executeTask(emitter);
                    }
                }
            });
        }
        return true;
    }

    void setMultiNum(@MultiNum int multiNum) {
        switch (multiNum) {
            case MultiNum.DEFAULT:
                this.multiNum = DEFAULT;
                break;
            case MultiNum.KEEP:
                break;
            case MultiNum.MAX:
                this.multiNum = MAX;
                break;
            case MultiNum.MIN:
                this.multiNum = MIN;
                break;
            default:
                this.multiNum = multiNum;
                break;
        }
    }
}
