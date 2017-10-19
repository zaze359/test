package com.zaze.utils.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 15:19
 */
public class TaskEntity {
    private String taskId;
    private String jsonData;
    private long loopTime;
    private long executeTime;

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

    public long getLoopTime() {
        return loopTime;
    }

    public TaskEntity setLoopTime(long loopTime) {
        this.loopTime = loopTime;
        return this;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public TaskEntity setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public String getJsonData() {
        return jsonData;
    }

    public TaskEntity setJsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }
}
