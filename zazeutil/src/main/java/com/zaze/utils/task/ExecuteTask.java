package com.zaze.utils.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
public class ExecuteTask extends TaskEntity {
    //
    private ConcurrentHashMap<String, TaskCallback> callbackMap;

    public ExecuteTask(TaskEntity entity) {
        setTaskId(entity.getTaskId());
        setAction(entity.getAction());
        setJsonData(entity.getJsonData());
        setExecuteTime(entity.getExecuteTime());
        setLoopTime(entity.getLoopTime());
    }

    public void addCallback(TaskCallback callback, boolean multiCallback) {
        if (callbackMap == null) {
            callbackMap = new ConcurrentHashMap<>();
        }
        if (callback == null) {
            return;
        }
        String key;
        if (multiCallback) {
            key = String.valueOf(callback.hashCode());
        } else {
            callbackMap.clear();
            key = "singleCallback";
        }
        callbackMap.put(key, callback);
    }

    public ConcurrentHashMap<String, TaskCallback> getCallbackMap() {
        return callbackMap;
    }

}