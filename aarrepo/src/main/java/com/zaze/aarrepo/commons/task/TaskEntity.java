package com.zaze.aarrepo.commons.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 15:19
 */
public class TaskEntity {
    private String action;
    private long loopTime;

    private long executeTime;

    public TaskEntity() {
    }

    public TaskEntity(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public TaskEntity setAction(String action) {
        this.action = action;
        return this;
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
}
