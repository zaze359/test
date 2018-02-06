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
    protected void executeActual(Executor<TaskEntity> executor) {
        // 同步执行需要注意回调嵌套
        executeTask(getTaskPool(), false, executor);
//        TaskPool taskPool = getTaskPool();
//        if (taskPool instanceof SyncTaskPool) {
//        } else if (taskPool instanceof FilterTaskPool) {
//            ZLog.i(ZTag.TAG_TASK, "转换为同步单任务执行模式(%s)", poolTag);
//            executeTask(((FilterTaskPool) taskPool).getTaskPool(), true, executor);
//        }
    }

    /**
     * 执行任务
     *
     * @param taskPool taskPool
     * @param replace  是否替换线程池
     */
    protected void executeTask(TaskPool taskPool, boolean replace, Executor<TaskEntity> executor) {
        if (taskPool != null) {
            if (taskPool.isEmpty()) {
                removeTaskPool();
            } else {
                if (replace) {
                    putTaskPool(taskPool);
                }
                TaskEmitter emitter = taskPool.getEmitter();
                if (executor != null) {
                    emitter.setExecutor(executor);
                    executor.onStart();
                }
                taskPool.executeTask(emitter);
            }
        }
    }
}
