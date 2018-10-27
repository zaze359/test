package com.zaze.utils.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 15:19
 */
public abstract class TaskEntity implements Executor<TaskEntity> {
    private String taskId;
    private String jsonData;

    public TaskEntity() {
    }

    public TaskEntity(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public TaskEntity setJsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onComplete() {

    }
}
