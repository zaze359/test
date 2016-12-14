package com.zz.library.commons.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 15:19
 */
public class TaskEntity {
    private String action;

    private long loopTime;

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
}
