package com.zaze.aarrepo.commons.task;

import java.util.HashMap;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
class ExecuteTask extends TaskEntity {
    //
    private HashMap<String, TaskCallback> callbackMap;

    public ExecuteTask(TaskEntity entity) {
        setAction(entity.getAction());
        setLoopTime(entity.getLoopTime());
    }

    public void addCallback(TaskCallback callback) {
        if (callbackMap == null) {
            callbackMap = new HashMap<>();
        }
        if (callback == null) {
            return;
        }
        String key = String.valueOf(callback.hashCode());
        if (!callbackMap.containsKey(key)) {
            callbackMap.put(key, callback);
        }
    }

    public HashMap<String, TaskCallback> getCallbackMap() {
        return callbackMap;
    }

}