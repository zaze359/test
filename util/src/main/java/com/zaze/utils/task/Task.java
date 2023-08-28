package com.zaze.utils.task;

import com.zaze.utils.task.executor.TaskPool;

import java.util.Collection;

/**
 * Description : Task的作用了为了方便的创建线程池来管理并执行任务，并且支持自由的切换线程池。
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
        return new TaskCreate<>(taskTag);
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

    @Override
    public void execute() {
        executeActual();
    }

    /**
     * 实际执行方法
     */
    protected abstract void executeActual();

    // --------------------------------------------------

    public static void clear() {
        TaskPusher.clearAllPoll();
    }

    public static void setNeedLog(boolean needLog) {
        Task.needLog = needLog;
        TaskPool.setNeedLog(needLog);
    }


}
