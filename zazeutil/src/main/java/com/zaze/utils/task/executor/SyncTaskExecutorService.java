package com.zaze.utils.task.executor;


import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.ExecuteTask;
import com.zaze.utils.task.TaskCallback;
import com.zaze.utils.task.TaskEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    /**
     * key : taskId
     */
    private final ConcurrentHashMap<String, ExecuteTask> taskMap = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> taskIdQueue = new ConcurrentLinkedQueue<>();
    /**
     * 当前正在执行的任务
     */
    private final Set<String> currentTaskSet = Collections.synchronizedSet(new HashSet<String>());

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
                            ZLog.i(ZTag.TAG_TASK, "执行任务(%s);剩余任务(%s)", executeTask.getTaskId(), taskIdQueue.size());
                        }
                        String taskId = executeTask.getTaskId();
                        currentTaskSet.add(taskId);
                        callback.onExecute(executeTask);
                        currentTaskSet.remove(taskId);
                    }
                }
            }
            return true;
        }
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "该任务池中, 所有任务已经执行完毕！");
        }
        return false;
    }

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    @Override
    public void pushTask(TaskEntity entity, TaskCallback callback) {
        pushTask(entity, callback, false, false);
    }

    @Override
    public void addFirst(TaskEntity entity, TaskCallback callback) {
        pushTask(entity, callback, false, true);
    }

    /**
     * @param entity             任务
     * @param callback           回调
     * @param isMultiplyCallback true 同个任务有多个回调,false 同个任务只保留最近一个回调
     */
    private void pushTask(TaskEntity entity, TaskCallback callback, boolean isMultiplyCallback, boolean addFirst) {
        if (entity == null) {
            return;
        }
        String taskId = entity.getTaskId();
        if (currentTaskSet.contains(ZStringUtil.parseString(taskId))) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "该任务正在执行中(%s)", taskId);
            }
            return;
        }
        // --------------------------------------------------
        if (ZStringUtil.isEmpty(taskId)) {
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
        if (addFirst) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "优先执行该任务(%s)", taskId);
            }
            if (taskIdQueue.contains(taskId)) {
                taskIdQueue.remove(taskId);
            }
            List<String> list = new ArrayList<>();
            list.add(taskId);
            for (String task : taskIdQueue) {
                list.add(task);
            }
            taskIdQueue.clear();
            taskIdQueue.addAll(list);
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "任务置顶成功(%s)", taskId);
            }
        } else {
            if (!taskIdQueue.contains(taskId)) {
                taskIdQueue.add(taskId);
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "添加任务成功(%s)", taskId);
                }
            } else {
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "任务已存在(%s)", taskId);
                }
            }
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
                if (!ZStringUtil.isEmpty(taskId) && taskMap.containsKey(taskId)) {
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
        currentTaskSet.clear();
    }

    // ------------------------------------------------
}
