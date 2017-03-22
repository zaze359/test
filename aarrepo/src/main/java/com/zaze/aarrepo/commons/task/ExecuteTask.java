package com.zaze.aarrepo.commons.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:48
 */
class ExecuteTask extends TaskEntity {
    //
    private boolean multiCallback = false;
    private ConcurrentHashMap<String, TaskCallback> callbackMap;

    public void setMultiCallback(boolean multiCallback) {
        this.multiCallback = multiCallback;
    }

    public ExecuteTask(TaskEntity entity) {
        setAction(entity.getAction());
        setLoopTime(entity.getLoopTime());
    }

    public void addCallback(TaskCallback callback) {
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