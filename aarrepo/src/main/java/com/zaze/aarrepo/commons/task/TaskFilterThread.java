package com.zaze.aarrepo.commons.task;

import com.zaze.aarrepo.utils.JsonUtil;
import com.zaze.aarrepo.utils.StringUtil;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private static final ConcurrentLinkedQueue<TaskEntity> actionPool = new ConcurrentLinkedQueue<>();
    // --------------------------------
    /**
     * 快速任务(最常用的任务, 自动解绑)
     */
    private static final ConcurrentHashMap<String, TaskEntity> fastActionMap = new ConcurrentHashMap<>();

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
            taskFilterThread = new TaskFilterThread();
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
            HashMap<String, TaskEntity> taskMap = new HashMap<>();
            while (!actionPool.isEmpty()) {
                TaskEntity taskEntity = actionPool.poll();
//                String action = taskEntity.getAction();
                taskMap.put(taskEntity.getAction(), taskEntity);
            }
            for (String action : taskMap.keySet()) {
                TaskEntity taskEntity = taskMap.get(action);
                if (taskEntity.getExecuteTime() >= currentRunTime) {
                    TaskCallback callback = taskEntity.getCallback();
                    if (callback != null) {
                        callback.onExecute();
                    }
                }
            }
            lastRunTimeFast = currentRunTime;
        }
    }

    // ------------------------------------------------

    /**
     * 注册临时业务
     *
     * @param jsonStr jsonStr
     */
    private static void addTaskToPool(String jsonStr, TaskCallback callback) {
        TaskEntity taskEntity = JsonUtil.parseJson(jsonStr, TaskEntity.class);
        if (taskEntity != null) {
            String action = taskEntity.getAction();
            if (!StringUtil.stringIsNull(action)) {
                long loopTime = taskEntity.getLoopTime();
                if (loopTime == 0) {
                    loopTime = loopTimeFast;
                }
                taskEntity.setCallback(callback);
                taskEntity.setExecuteTime(System.currentTimeMillis() + loopTime);
                actionPool.add(taskEntity);
            }
        } else {
            // TODO 格式不对提示
        }
    }

    // ------------------------------------------------
    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }
    // ------------------------------------------------
}
