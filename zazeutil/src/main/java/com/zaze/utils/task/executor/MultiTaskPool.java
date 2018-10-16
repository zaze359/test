package com.zaze.utils.task.executor;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.MultiNum;

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
    private int notifyCount = 1;
    private MyThreadPoolExecutor multiExecutor;
    private @MultiNum
    int multiNum = DEFAULT;

    public static MultiTaskPool newInstance(TaskPool taskPool) {
        return new MultiTaskPool(taskPool);
    }

    private MultiTaskPool(TaskPool taskPool) {
        super(taskPool);
        multiExecutor = new MyThreadPoolExecutor(MIN, DEFAULT, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "MultiTaskPool");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
        multiExecutor.allowCoreThreadTimeOut(true);
    }

    @Override
    public boolean executeTask() {
        execute(multiNum);
        return true;
    }


    public void notifyExecute() {
        execute(notifyCount);
    }

    private void execute(int count) {
        for (int i = 0; i < count; i++) {
            multiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isStop) {
                        if (needLog) {
                            ZLog.i(ZTag.TAG_TASK, "MultiTaskPool");
                        }
                        MultiTaskPool.super.executeTask();
                    }
                }
            });
        }
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
            case MultiNum.FIVE:
                return MultiNum.FIVE;
            case MultiNum.FOUR:
                return MultiNum.FOUR;
            case MultiNum.THREE:
                return MultiNum.THREE;
            case MultiNum.TWO:
                return MultiNum.TWO;
            default:
                return MAX;
        }
    }
}
