package com.zaze.utils.task;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.executor.FilterTaskPool;
import com.zaze.utils.task.executor.SyncTaskPool;
import com.zaze.utils.task.executor.TaskPool;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:14
 */
public class TaskCreate<T> extends TaskPusher<T> {

    public TaskCreate(String taskTag) {
        super(taskTag);
    }

    @Override
    public Task<T> executeOn(int schedulers) {
        switch (schedulers) {
            case TaskSchedulers.ASYNC:
                return new TaskAsync<>(this);
            case TaskSchedulers.ASYNC_AUTO:
                return new TaskAsyncAuto<>(this);
            case TaskSchedulers.ASYNC_MULTI:
                return new TaskAsyncMulti<>(this);
            default:
                return this;
        }
    }

    @Override
    protected void executeActual(Executor<TaskEntity> executor) {
        // 同步执行执行指定标签的下一个任务(需要注意回调嵌套)
        TaskPool taskPool = getTaskPool();
        if (taskPool instanceof SyncTaskPool) {
            executeTask(taskPool, false, executor);
        } else if (taskPool instanceof FilterTaskPool) {
            ZLog.i(ZTag.TAG_TASK, "转换为同步单任务执行模式(%s)", poolTag);
            executeTask(((FilterTaskPool) taskPool).getTaskPool(), true, executor);
        }
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
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "任务池(%s)已经执行完毕, 移除！", poolTag);
                }
                removeTaskPool();
            } else {
                if (replace) {
                    putTaskPool(taskPool);
                }
                TaskEmitter emitter = new TaskEmitter(executor);
                executor.onStart();
                taskPool.executeTask(emitter);
            }
        }
    }
}
