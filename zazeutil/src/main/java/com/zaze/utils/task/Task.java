package com.zaze.utils.task;

import com.zaze.utils.task.executor.TaskPool;

import java.util.Collection;

/**
 * Description :
 * TODO : 一个任务标签对应一个线程池, 下一步考虑修改为线程，固定线程池管理
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:20
 */
public abstract class Task<T> implements TaskSource {
    protected static boolean needLog;

    /**
     * 默认标签
     */
    public static final String DEFAULT_TAG = "$%#zaze_default_#%$";
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

    public abstract TaskAsyncMulti<T> executeOnAsyncMulti();

    public abstract TaskAsync<T> executeOnAsync();

    public abstract TaskAsyncAuto<T> executeOnAsyncAuto();

    @Override
    public void execute(Executor<TaskEntity> executor) {
        executeActual(executor);
    }

    @Override
    public void notifyExecute() {
        executeActual(null);
    }

    /**
     * 实际执行方法
     *
     * @param executor executor
     */
    protected abstract void executeActual(Executor<TaskEntity> executor);

    // --------------------------------------------------

    public static void clear() {
        TaskPusher.clearAllPoll();
    }

    public static void setNeedLog(boolean needLog) {
        Task.needLog = needLog;
        TaskPool.setNeedLog(needLog);
    }


}
