package com.zaze.aarrepo.commons.task;


import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.utils.JsonUtil;
import com.zaze.aarrepo.utils.StringUtil;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
public class TaskFilterThread {
    // --------------------------------
    /**
     * 事件池
     */
//    private static final ConcurrentLinkedQueue<ExecuteTask> actionPool = new ConcurrentLinkedQueue<>();
    // --------------------------------
    /**
     * 快速任务(最常用的任务, 自动解绑)
     */
    private static final ConcurrentHashMap<String, ExecuteTask> fastTaskMap = new ConcurrentHashMap<>();

    // --------------------------------
    private static long lastRunTimeFast = 0L;
    // --------------------------------
    private static final long loopTimeFast = 300L;
    // --------------------------------
    private static boolean needLog = false;
    private static ExecutorService singleExecutor;

    private static TaskFilterThread taskFilterThread;

    public static TaskFilterThread getInstance() {
        if (taskFilterThread == null) {
            synchronized (TaskFilterThread.class) {
                if (taskFilterThread == null) {
                    taskFilterThread = new TaskFilterThread();
                }
            }
        }
        return taskFilterThread;
    }

    private TaskFilterThread() {
        singleExecutor = Executors.newSingleThreadExecutor();
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                runTime();
            }
        });
    }

    private void runTime() {
        while (true) {
            try {
                executeTask(System.currentTimeMillis());
                Thread.sleep(200L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void executeTask(long currentRunTime) {
        notifyFastTask(currentRunTime);
    }

    /**
     * 广播通知临时业务
     *
     * @param currentRunTime
     */
    private void notifyFastTask(long currentRunTime) {
        if (currentRunTime - lastRunTimeFast > loopTimeFast) {
            for (String action : fastTaskMap.keySet()) {
                ExecuteTask executeTask = fastTaskMap.get(action);
                if (currentRunTime >= executeTask.getExecuteTime()) {
                    HashMap<String, TaskCallback> callbackMap = executeTask.getCallbackMap();
                    if (!callbackMap.isEmpty()) {
                        for (String key : callbackMap.keySet()) {
                            callbackMap.get(key).onExecute();
                        }
                        fastTaskMap.remove(action);
                    }
                }
            }
            lastRunTimeFast = currentRunTime;
        }
    }

    // ------------------------------------------------

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    public void pushTask(TaskEntity entity, TaskCallback callback) {
        ExecuteTask newExecuteTask = new ExecuteTask(entity);
        String action = newExecuteTask.getAction();
        if (!StringUtil.isEmpty(action)) {
            ExecuteTask executeTask;
            if (fastTaskMap.containsKey(action)) {
                executeTask = fastTaskMap.get(action);
            } else {
                executeTask = newExecuteTask;
                long loopTime = executeTask.getLoopTime();
                if (loopTime == 0) {
                    loopTime = loopTimeFast;
                }
                executeTask.setExecuteTime(System.currentTimeMillis() + loopTime);
            }
            if (callback != null) {
                executeTask.addCallback(callback);
            }
            fastTaskMap.put(action, executeTask);
        }
    }

    /**
     * @param jsonStr  TaskEntity 子类json
     * @param callback TaskCallback
     */
    public void pushTask(String jsonStr, TaskCallback callback) {
        ExecuteTask newExecuteTask = JsonUtil.parseJson(jsonStr, ExecuteTask.class);
        if (newExecuteTask != null) {
            String action = newExecuteTask.getAction();
            if (!StringUtil.isEmpty(action)) {
                ExecuteTask executeTask;
                if (fastTaskMap.containsKey(action)) {
                    executeTask = fastTaskMap.get(action);
                } else {
                    executeTask = newExecuteTask;
                    long loopTime = executeTask.getLoopTime();
                    if (loopTime == 0) {
                        loopTime = loopTimeFast;
                    }
                    executeTask.setExecuteTime(System.currentTimeMillis() + loopTime);
                }
                if (callback != null) {
                    executeTask.addCallback(callback);
                }
                fastTaskMap.put(action, executeTask);
            }
        } else {
            LogKit.e("传入json数据 不是 TaskEntity 子类json");
            // TODO 格式不对提示
        }
    }

    // ------------------------------------------------
    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }
    // ------------------------------------------------
}
