package com.zaze.utils.task;

/**
 * Description : 任务源
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:33
 */
public interface TaskSource {

    /**
     * 外部调用执行任务接口
     *
     * @param executor 执行器
     */
    void execute(Executor<TaskEntity> executor);

    /**
     * 唤醒执行, 使用之前设置的callback 回掉
     */
    void notifyExecute();

}
