package com.zaze.utils.task;

/**
 * Description : 任务源
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:33
 */
public interface TaskSource<T> {
    /**
     * 添加任务(默认到末端)
     *
     * @param entity     entity
     * @param pushToHead 是否将任务添加到头部
     * @return Task
     */
    Task<T> pushTask(TaskEntity entity, boolean pushToHead);

    /**
     * 移除任务
     *
     * @param taskId taskId
     */
    Task<T> removeTask(String taskId);

    TaskAsyncMulti<T> executeOnAsyncMulti();

    TaskAsync<T> executeOnAsync();

    TaskAsyncAuto<T> executeOnAsyncAuto();

    /**
     * 外部调用执行任务接口
     */
    void execute();

    void clearPoll();

    /**
     * @return 是否为空
     */
    boolean isEmpty();

    /**
     * @return 是否空闲
     */
    boolean isIdle();
}
