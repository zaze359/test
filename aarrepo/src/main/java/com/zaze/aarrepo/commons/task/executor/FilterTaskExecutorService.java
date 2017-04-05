package com.zaze.aarrepo.commons.task.executor;

import com.zaze.aarrepo.commons.task.ExecuteTask;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-25 - 00:53
 */
public class FilterTaskExecutorService extends TaskExecutorService {
    protected TaskExecutorService taskExecutorService;

    public FilterTaskExecutorService(TaskExecutorService taskExecutorService) {
        this.taskExecutorService = taskExecutorService;
    }

    @Override
    public boolean executeNextTask() {
        return taskExecutorService != null && taskExecutorService.executeNextTask();
    }

    @Override
    public void pushTask(TaskEntity entity, TaskCallback callback) {
        if (taskExecutorService != null) {
            taskExecutorService.pushTask(entity, callback);
        }
    }

    @Override
    public ExecuteTask pollTask() {
        return taskExecutorService == null ? null : taskExecutorService.pollTask();
    }

    @Override
    public boolean isEmpty() {
        return taskExecutorService == null || taskExecutorService.isEmpty();
    }

    @Override
    public void clear() {
        if (taskExecutorService != null) {
            taskExecutorService.clear();
        }
    }
}
