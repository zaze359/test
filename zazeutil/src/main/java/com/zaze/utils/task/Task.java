package com.zaze.utils.task;

import android.support.annotation.NonNull;

import com.zaze.utils.task.executor.TaskPool;

import java.util.Collection;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:20
 */
public abstract class Task<T> implements TaskSource {
    /**
     * 默认标签
     */
    public static final String DEFAULT_TAG = "$%#zaze_default_#%$";

    protected static boolean needLog;

    /**
     * 任务分类到标签
     */
    protected final String poolTag;

    public Task(String poolTag) {
        this.poolTag = poolTag;
    }

    /**
     * 创建默认到任务组
     *
     * @param <T> t
     * @return 默认到任务组
     */
    public static <T> Task<T> create() {
        return create(DEFAULT_TAG);
    }

    /**
     * 创建指定标签到任务组
     *
     * @param taskTag poolTag
     * @param <T>     t
     * @return 指定标签到任务组
     */
    public static <T> Task<T> create(String taskTag) {
        return new TaskCreate<T>(taskTag);
    }

    /**
     * @param entity
     * @return
     */
    public Task<T> pushToHead(TaskEntity entity) {
        return pushTask(entity, true);
    }

    /**
     * @param list
     * @return
     */
    public Task<T> push(Collection<TaskEntity> list) {
        for (TaskEntity entity : list) {
            push(entity);
        }
        return this;
    }

    /**
     * @param entity entity
     * @return Task
     */
    public Task<T> push(TaskEntity entity) {
        return pushTask(entity, false);
    }

    /**
     * 添加任务(默认到末端)
     *
     * @param entity     entity
     * @param pushToHead 是否将任务添加到头部
     * @return Task
     */
    protected abstract Task<T> pushTask(TaskEntity entity, boolean pushToHead);

    /**
     * 指定运行到线程池
     *
     * @param schedulers schedulers
     * @return Task
     */
    public abstract Task<T> executeOn(@TaskSchedulers int schedulers);

    @Override
    public void execute(@NonNull Executor<TaskEntity> executor) {
        executeActual(executor);
    }

    /**
     * 实际执行方法
     *
     * @param executor executor
     */
    protected abstract void executeActual(Executor<TaskEntity> executor);

    public static void clear() {
        TaskPusher.clearAllPoll();
    }

    public static void setNeedLog(boolean needLog) {
        Task.needLog = needLog;
        TaskPool.setNeedLog(needLog);
    }


}
