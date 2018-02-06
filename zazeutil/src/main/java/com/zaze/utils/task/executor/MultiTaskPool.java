package com.zaze.utils.task.executor;


import android.support.annotation.NonNull;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.MultiNum;
import com.zaze.utils.task.TaskEmitter;
import com.zaze.utils.task.TaskEntity;

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
    private static final int DEFAULT = 2;
    private static final int MAX = 6;
    private static final long KEEP_ALIVE_TIME = 60L;
    private int notifyCount = DEFAULT;

    private int currentNum = 0;

    private MyThreadPoolExecutor multiExecutor;
    private volatile @MultiNum
    int multiNum = DEFAULT;

    public static MultiTaskPool newInstance(TaskPool taskPool) {
        return new MultiTaskPool(taskPool);
    }

    private MultiTaskPool(TaskPool taskPool) {
        super(taskPool);
        multiExecutor = new MyThreadPoolExecutor(MIN, DEFAULT, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
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
    public boolean executeTask(@NonNull final TaskEmitter emitter) {
        for (; currentNum < notifyCount; currentNum++) {
            multiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isStop) {
                        if (needLog) {
                            ZLog.i(ZTag.TAG_TASK, "MultiTaskPool");
                        }
                        MultiTaskPool.super.executeTask(new TaskEmitter() {
                            @Override
                            public void onError(Throwable error) {
                                emitter.onError(error);
                            }

                            @Override
                            public void onExecute(@NonNull TaskEntity value) {
                                emitter.onExecute(value);
                            }

                            @Override
                            public void onComplete() {
                                currentNum--;
                                emitter.onComplete();
                            }
                        });
                    }
                }
            });
        }
        return true;
    }

    public void setMultiNum(@MultiNum int multiNum) {
        int num = transNum(multiNum);
        if (num != MultiNum.KEEP) {
            this.multiNum = num;
        }
        multiExecutor.setMaximumPoolSize(this.multiNum);
    }


    public void setNotifyCount(@MultiNum int multiNum) {
        int num = transNum(multiNum);
        if (num != MultiNum.KEEP) {
            this.notifyCount = num;
        }
    }

    private int transNum(@MultiNum int multiNum) {
        switch (multiNum) {
            case MultiNum.DEFAULT:
                return DEFAULT;
            case MultiNum.KEEP:
                return MultiNum.KEEP;
            case MultiNum.MAX:
                return MAX;
            case MultiNum.MIN:
                return MIN;
            default:
                return multiNum;
        }
    }
}
