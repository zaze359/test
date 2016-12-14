package com.zaze.aarrepo.commons.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
class TaskEntity {
    private String action;

    private long loopTime;
    //
    private long executeTime;

    private TaskCallback callback;

    public TaskCallback getCallback() {
        return callback;
    }

    public void setCallback(TaskCallback callback) {
        this.callback = callback;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getLoopTime() {
        return loopTime;
    }

    public void setLoopTime(long loopTime) {
        this.loopTime = loopTime;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }
}