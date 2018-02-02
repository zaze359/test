package com.zaze.utils.task;

import android.support.annotation.NonNull;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 17:19
 */
public class TaskEmitter implements Emitter<TaskEntity> {

    private Executor<TaskEntity> executor;

    public TaskEmitter(@NonNull Executor<TaskEntity> executor) {
        this.executor = executor;
    }

    @Override
    public void onError(@NonNull Throwable error) {
        executor.onError();
    }

    @Override
    public void onExecute(@NonNull TaskEntity value) {
        try {
            executor.onExecute(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        executor.onComplete();
    }
}
