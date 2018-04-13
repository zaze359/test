package com.zaze.utils.task;

import com.zaze.utils.task.executor.TaskPool;

/**
 * Description : 创建任务类
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:14
 */
public class TaskCreate<T> extends TaskPusher<T> {

    public TaskCreate(String taskTag) {
        super(taskTag);
    }

    @Override
    public TaskAsyncMulti<T> executeOnAsyncMulti() {
        return new TaskAsyncMulti<>(this);
    }

    @Override
    public TaskAsync<T> executeOnAsync() {
        return new TaskAsync<>(this);
    }

    @Override
    public TaskAsyncAuto<T> executeOnAsyncAuto() {
        return new TaskAsyncAuto<>(this);
    }

    @Override
    protected void executeActual() {
        executeTask(getOrCreatePool(), false);
    }

    /**
     * 执行任务
     *
     * @param taskPool taskPool
     * @param replace  是否替换线程池
     */
    protected void executeTask(TaskPool taskPool, boolean replace) {
        if (taskPool != null) {
            if (taskPool.isEmpty()) {
                removePool();
            } else {
                if (replace) {
                    putPool(taskPool);
                }
                taskPool.executeTask();
            }
        }
    }
}
