package com.zaze.aarrepo.commons.task.queue;


import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.commons.task.ExecuteTask;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class TaskExecutorService {
    private static boolean needLog = false;
    // --------------------------------
    private final ConcurrentLinkedQueue<String> taskIdQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, ExecuteTask> taskMap = new ConcurrentHashMap<>();
    // --------------------------------
    private ExecutorService singleExecutor;

    public TaskExecutorService() {
    }

    /**
     * 执行下一个任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    public boolean executeNextTask() {
        return executeTask(pollTask());
    }

    /**
     * 自动依次执行所有任务
     */
    public void autoExecuteTask() {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!taskIdQueue.isEmpty()) {
                        executeNextTask();
                        Thread.sleep(200L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 中断取消所有任务
     */
    public void shutdown() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "中断取消改任务池中的所有剩余任务。。。");
        }
        taskIdQueue.clear();
        taskMap.clear();
        singleExecutor.shutdown();
        singleExecutor = null;

    }

    /**
     * 执行任务
     *
     * @param executeTask 任务
     * @return 是否有任务可以执行
     */
    private boolean executeTask(ExecuteTask executeTask) {
        if (executeTask != null) {
            ConcurrentHashMap<String, TaskCallback> callbackMap = executeTask.getCallbackMap();
            if (!callbackMap.isEmpty()) {
                for (String key : callbackMap.keySet()) {
                    TaskCallback callback = callbackMap.get(key);
                    callbackMap.remove(key);
                    if (callback != null) {
                        if (needLog) {
                            ZLog.i(ZTag.TAG_TASK, "执行任务(%s)", executeTask.getTaskId());
                        }
                        callback.onExecute(executeTask);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    public void pushTask(TaskEntity entity, TaskCallback callback) {
        pushTask(entity, callback, false);
    }

    /**
     * @param entity             任务
     * @param callback           回调
     * @param isMultiplyCallback true 同个任务有多个回调,false 同个任务只保留最近一个回调
     */
    public void pushTask(TaskEntity entity, TaskCallback callback, boolean isMultiplyCallback) {
        if (entity == null) {
            return;
        }
        String taskId = entity.getTaskId();
        if (StringUtil.isEmpty(taskId)) {
            taskId = String.valueOf(entity.hashCode());
            entity.setTaskId(taskId);
        }
        ExecuteTask newExecuteTask = new ExecuteTask(entity);
        ExecuteTask executeTask;
        if (taskMap.containsKey(taskId)) {
            executeTask = taskMap.get(taskId);
        } else {
            executeTask = newExecuteTask;
        }
        if (callback != null) {
            executeTask.addCallback(callback, isMultiplyCallback);
        }
        taskMap.put(taskId, executeTask);
        if (!taskIdQueue.contains(taskId)) {
            taskIdQueue.add(taskId);
        }
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "添加任务(%s)", taskId);
        }
    }

    /**
     * @return 取一个任务
     */
    public ExecuteTask pollTask() {
        if (!taskIdQueue.isEmpty() && !taskMap.isEmpty()) {
            while (taskIdQueue.peek() != null) {
                String taskId = taskIdQueue.poll();
                if (taskMap.containsKey(taskId)) {
                    ExecuteTask executeTask = taskMap.get(taskId);
                    taskMap.remove(taskId);
                    if (needLog) {
                        ZLog.i(ZTag.TAG_TASK, "提取执行任务(%s)", taskId);
                    }
                    return executeTask;
                }
            }
        }
        return null;
    }

    // ------------------------------------------------

    public static void setNeedLog(boolean isNeedLog) {
        TaskExecutorService.needLog = isNeedLog;
    }
    // ------------------------------------------------
}
