package com.zaze.utils.task;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 17:19
 */
public class TaskEmitter implements Emitter<TaskEntity> {

    private Executor<TaskEntity> executor;

    public TaskEmitter() {
    }

    public TaskEmitter(Executor<TaskEntity> executor) {
        this.executor = executor;
    }

    @Override
    public void onError(Throwable error) {
        if (executor != null) {
            executor.onError();
        }
    }

    @Override
    public void onExecute(TaskEntity value) throws Exception {
        if (executor != null) {
            executor.onExecute(value);
        }
    }

    @Override
    public void onComplete() {
        if (executor != null) {
            executor.onComplete();
        }
    }

    public void setExecutor(Executor<TaskEntity> executor) {
        this.executor = executor;
    }
}
