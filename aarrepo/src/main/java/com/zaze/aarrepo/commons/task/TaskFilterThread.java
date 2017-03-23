package com.zaze.aarrepo.commons.task;


import com.zaze.aarrepo.commons.task.queue.TaskQueueManager;
import com.zaze.aarrepo.utils.StringUtil;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 * to user TaskQueueManager
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 * @see TaskQueueManager
 */
@Deprecated
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
    private static final ConcurrentHashMap<String, ExecuteTask> fastTaskMap = new ConcurrentHashMap<>(10000);

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
        //
        // notify
    }

    /**
     * 广播通知临时业务
     *
     * @param currentRunTime
     */
    private void notifyFastTask(long currentRunTime) {
        if (currentRunTime - lastRunTimeFast > loopTimeFast) {
            Iterator<String> taskIterator = fastTaskMap.keySet().iterator();
            while (taskIterator.hasNext()) {
                String action = taskIterator.next();
                ExecuteTask executeTask = fastTaskMap.get(action);
                if (currentRunTime >= executeTask.getExecuteTime()) {
                    ConcurrentHashMap<String, TaskCallback> callbackMap = executeTask.getCallbackMap();
                    if (!callbackMap.isEmpty()) {
                        Iterator<String> iterator = callbackMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            callbackMap.get(key).onExecute(executeTask);
                            callbackMap.remove(key);
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
        pushTask(entity, callback, false);
    }

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    public void pushTask(TaskEntity entity, TaskCallback callback, boolean isMultiply) {
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
                executeTask.addCallback(callback, isMultiply);
            }
            fastTaskMap.put(action, executeTask);
        }
    }

    // ------------------------------------------------
    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }
    // ------------------------------------------------
}
