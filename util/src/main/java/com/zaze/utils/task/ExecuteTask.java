package com.zaze.utils.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
public class ExecuteTask extends TaskEntity {
    private Executor<TaskEntity> executor;

    public ExecuteTask(TaskEntity entity) {
        if (entity != null) {
            setTaskId(entity.getTaskId());
            setJsonData(entity.getJsonData());
            executor = entity;
        }
    }

    @Override
    public void onExecute(TaskEntity task) throws Exception {
        if (executor != null) {
            executor.onExecute(task);
        }
    }

    @Override
    public void onStart() {
        if (executor != null) {
            executor.onStart();
        }
    }

    @Override
    public void onError() {
        if (executor != null) {
            executor.onError();
        }
    }

    @Override
    public void onComplete() {
        if (executor != null) {
            executor.onComplete();
        }
    }
}