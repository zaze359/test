package com.zaze.utils.task.executor;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class AsyncTaskExecutorService extends FilterTaskExecutorService {
    protected ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AsyncTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
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
