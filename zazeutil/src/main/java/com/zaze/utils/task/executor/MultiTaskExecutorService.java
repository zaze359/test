package com.zaze.utils.task.executor;


import com.zaze.utils.ZCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : 多任务池服务
 *
 * @author zaze
 * @version 2017/3/25 - 上午12:25 1.0
 */
class MultiTaskExecutorService extends FilterTaskExecutorService {
    private static ExecutorService multiExecutor;

    public MultiTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
    }

    /**
     * 执行一批多个任务(最大同时执行上限10)
     *
     * @param num 执行任务数
     */
    public void multiExecuteTask(int num, final ZCallback<Boolean> callback) {
        if (multiExecutor == null) {
            multiExecutor = Executors.newFixedThreadPool(10);
        }
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
