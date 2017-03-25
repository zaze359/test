package com.zaze.aarrepo.commons.task.executor;


import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.commons.task.ExecuteTask;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class SyncTaskExecutorService extends TaskExecutorService {
    private final ConcurrentLinkedQueue<String> taskIdQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, ExecuteTask> taskMap = new ConcurrentHashMap<>();

    /**
     * 执行下一个任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    @Override
    public boolean executeNextTask() {
        return executeTask(pollTask());
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
                            ZLog.i(ZTag.TAG_TASK, "剩余任务(%s)", taskIdQueue.size());
                        }
                        callback.onExecute(executeTask);
                    }
                }
            }
            return true;
        }
        ZLog.i(ZTag.TAG_TASK, "该任务池中, 所有任务已经执行完毕！");
        return false;
    }

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    @Override
    public void pushTask(TaskEntity entity, TaskCallback callback) {
        pushTask(entity, callback, false);
    }

    /**
     * @param entity             任务
     * @param callback           回调
     * @param isMultiplyCallback true 同个任务有多个回调,false 同个任务只保留最近一个回调
     */
    private void pushTask(TaskEntity entity, TaskCallback callback, boolean isMultiplyCallback) {
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
    @Override
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

    @Override
    public boolean isEmpty() {
        return taskIdQueue.isEmpty();
    }

    @Override
    public void clear() {
        taskIdQueue.clear();
        taskMap.clear();
    }

    // ------------------------------------------------
}
