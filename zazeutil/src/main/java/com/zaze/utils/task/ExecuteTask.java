package com.zaze.utils.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
public class ExecuteTask extends TaskEntity {

    public ExecuteTask(TaskEntity entity) {
        setTaskId(entity.getTaskId());
        setJsonData(entity.getJsonData());
        setExecuteTime(entity.getExecuteTime());
        setLoopTime(entity.getLoopTime());
    }
}