package com.zaze.utils.task;

import android.support.annotation.NonNull;

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
    void execute(@NonNull Executor<TaskEntity> executor);
}
