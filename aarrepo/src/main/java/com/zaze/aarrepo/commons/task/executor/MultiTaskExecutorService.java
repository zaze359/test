package com.zaze.aarrepo.commons.task.executor;


import com.zaze.aarrepo.utils.iface.ECallback;

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
     * 执行一批多个任务
     *
     * @param num 执行任务数(最大上限20)
     */
    public void multiExecuteTask(int num, final ECallback<Boolean> callback) {
        if (num > 20) {
            num = 20;
        }
        multiExecutor = Executors.newFixedThreadPool(num);
        for (int i = 0; i < num; i++) {
            multiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onNext(executeNextTask());
                    }
                }
            });
        }
    }
}
