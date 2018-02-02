package com.zaze.utils.task.executor;


import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.ExecuteTask;
import com.zaze.utils.task.TaskEmitter;
import com.zaze.utils.task.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
public class SyncTaskPool extends TaskPool {

    /**
     * key : taskId
     */
    private final ConcurrentLinkedQueue<String> taskIdQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, ExecuteTask> taskMap = new ConcurrentHashMap<>();
    /**
     * 当前正在执行的任务
     */
//    private final Set<String> currentTaskSet = Collections.synchronizedSet(new HashSet<String>());
    private final ConcurrentSkipListSet<String> currentTaskSet = new ConcurrentSkipListSet<>();

    public SyncTaskPool() {
        isStop = false;
    }

    /**
     * 执行下一个任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    @Override
    public boolean executeTask(TaskEmitter emitter) {
        return executeTask(pollTask(), emitter);
    }

    /**
     * 执行任务
     *
     * @param executeTask 任务
     * @return 是否有任务可以执行
     */
    private boolean executeTask(ExecuteTask executeTask, TaskEmitter emitter) {
        if (isStop) {
            ZLog.i(ZTag.TAG_TASK, "该任务池已停止执行！");
            return false;
        }
        if (executeTask != null && emitter != null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "执行任务(%s);剩余任务(%s)", executeTask.getTaskId(), taskIdQueue.size());
            }
            String taskId = executeTask.getTaskId();
            currentTaskSet.add(taskId);
            emitter.onExecute(executeTask);
            currentTaskSet.remove(taskId);
            emitter.onComplete();
            return true;
        }
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "该任务池中, 所有任务已经执行完毕！");
        }
        return false;
    }

    @Override
    public void pushTask(TaskEntity entity, boolean addFirst) {
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
            list.addAll(taskIdQueue);
            taskIdQueue.clear();
            taskIdQueue.addAll(list);
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "任务置顶成功(%s)", taskId);
            }
        } else {
            if (!taskIdQueue.contains(taskId)) {
                taskIdQueue.add(taskId);
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "添加任务(%s) : 成功", taskId);
                }
            } else {
                if (needLog) {
                    ZLog.w(ZTag.TAG_TASK, "已存在相同任务(%s), 更新为最新", taskId);
                }
            }
//            if (taskIdQueue.contains(taskId)) {
//                if (needLog) {
//                    ZLog.w(ZTag.TAG_TASK, "已存在相同任务(%s), 更新为最新", taskId);
//                }
//            } else {
//                if (needLog) {
//                    ZLog.i(ZTag.TAG_TASK, "添加任务(%s) : 成功", taskId);
//                }
//            }
//            taskIdQueue.remove(taskId);
//            taskIdQueue.add(taskId);
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
                        ZLog.i(ZTag.TAG_TASK, "提取执行 : 任务(%s)", taskId);
                    }
                    return executeTask;
                }
            }
        }
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "没有需要执行的任务！");
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return taskIdQueue.isEmpty();
    }

    @Override
    public void stop() {
        isStop = false;
    }

    @Override
    public void clear() {
        taskIdQueue.clear();
        taskMap.clear();
        currentTaskSet.clear();
    }

    // --------------------------------------------------
}
