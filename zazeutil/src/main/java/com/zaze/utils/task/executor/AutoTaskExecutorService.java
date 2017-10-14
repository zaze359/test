package com.zaze.utils.task.executor;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.Executors;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class AutoTaskExecutorService extends AsyncTaskExecutorService {
    private boolean isRunning = true;

    public AutoTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);

    }

    /**
     * 自动依次执行所有任务
     */
    public boolean autoExecute() {
        isRunning = true;
        return executeAsyncTask();
    }

    @Override
    public boolean executeAsyncTask() {
        if (taskExecutorService == null) {
            return false;
        }
        if (autoExecutor == null && !taskExecutorService.isEmpty()) {
            autoExecutor = Executors.newSingleThreadExecutor();
            autoExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    AutoTaskExecutorService.this.run();
                }
            });
            return true;
        } else {
            return isRunning;
        }
    }

    @Override
    protected void run() {
        try {
            do {
                if (taskExecutorService.isEmpty()) {
                    Thread.sleep(5000L);
                } else {
                    taskExecutorService.executeNextTask();
                    Thread.sleep(100L);
                }
            } while (isRunning);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中断取消所有任务
     */
    public void shutdown() {
        isRunning = false;
        if (autoExecutor != null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "中断取消 自动执行任务池中的所有剩余任务!");
            }
            clear();
            autoExecutor.shutdown();
            autoExecutor = null;
        } else {
            ZLog.i(ZTag.TAG_TASK, "任务池不存在, 不需要中断!");
        }
    }
}
