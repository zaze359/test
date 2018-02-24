package com.zaze.utils.task.executor;


import com.zaze.utils.task.ExecuteTask;
import com.zaze.utils.task.TaskEntity;

import java.util.concurrent.ExecutorService;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-25 - 00:53
 */
public class FilterTaskPool extends TaskPool {
    ExecutorService executorService;
    TaskPool taskPool;

    public FilterTaskPool(TaskPool taskPool) {
        if (taskPool instanceof FilterTaskPool) {
            // 若存在之前的则先暂停之前
            taskPool.stop();
            this.taskPool = ((FilterTaskPool) taskPool).getTaskPool();
        } else {
            this.taskPool = taskPool;
        }
        if (taskPool != null) {
            taskPool.isStop = false;
        }
        this.isStop = false;
    }

    @Override
    public boolean executeTask() {
        return taskPool != null && taskPool.executeTask();
    }

    @Override
    public void pushTask(TaskEntity entity, boolean toHead) {
        if (taskPool != null) {
            taskPool.pushTask(entity, toHead);
        }
    }

    @Override
    public void removeTask(String taskId) {
        if (taskPool != null) {
            taskPool.removeTask(taskId);
        }
    }

    @Override
    public ExecuteTask pollTask() {
        return taskPool == null ? null : taskPool.pollTask();
    }

    @Override
    public boolean isEmpty() {
        return taskPool == null || taskPool.isEmpty();
    }

    @Override
    public void stop() {
        super.stop();
        if (executorService != null) {
            executorService.shutdown();
        }
        if (taskPool != null) {
            taskPool.stop();
        }
    }

    @Override
    public void clear() {
        super.clear();
        if (taskPool != null) {
            taskPool.clear();
        }
    }

    public TaskPool getTaskPool() {
        return taskPool;
    }
}
