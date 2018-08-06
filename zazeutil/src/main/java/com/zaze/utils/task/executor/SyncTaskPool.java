package com.zaze.utils.task.executor;


import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.Emitter;
import com.zaze.utils.task.ExecuteTask;
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
     * 需要执行的任务id队列
     */
    private final ConcurrentLinkedQueue<String> taskIdQueue = new ConcurrentLinkedQueue<>();

    /**
     * 所有任务的集合
     */
    private final ConcurrentHashMap<String, ExecuteTask> taskMap = new ConcurrentHashMap<>();
    /**
     * 当前正在执行的任务的集合
     */
    private final ConcurrentSkipListSet<String> currentTaskSet = new ConcurrentSkipListSet<>();
//    private final Set<String> currentTaskSet = Collections.synchronizedSet(new HashSet<String>());

    public SyncTaskPool() {
        isStop = false;
    }

    /**
     * 执行下一个任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    @Override
    public boolean executeTask() {
        return executeTask(pollTask());
    }

    /**
     * 执行任务
     *
     * @param executeTask 任务
     * @return 是否有任务可以执行
     */
    private boolean executeTask(ExecuteTask executeTask) {
        Emitter<TaskEntity> emitter = getEmitter(executeTask);
        if (isStop) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "该任务池已停止执行！");
            }
            return false;
        }
        if (executeTask == null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "该任务池已经执行完毕！");
            }
            return false;
        }
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

    @Override
    public void pushTask(TaskEntity entity, boolean addFirst) {
        if (entity == null) {
            return;
        }
        String taskId = entity.getTaskId();
        if (ZStringUtil.isEmpty(taskId)) {
            taskId = String.valueOf(entity.hashCode());
            entity.setTaskId(taskId);
        }
        if (currentTaskSet.contains(ZStringUtil.parseString(taskId))) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "该任务正在执行中(%s)", taskId);
            }
            return;
        }
        taskMap.put(taskId, new ExecuteTask(entity));
        if (addFirst) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "优先执行该任务(%s)", taskId);
            }
            removeTask(taskId);
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
        }
    }

    @Override
    public void removeTask(String taskId) {
        if (taskIdQueue.contains(taskId)) {
            taskIdQueue.remove(taskId);
        }
    }

    /**
     * @return 取一个任务
     */
    @Override
    public ExecuteTask pollTask() {
        if (!isEmpty()) {
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
        return taskIdQueue.isEmpty() || taskMap.isEmpty();
    }

    @Override
    public boolean isIdle() {
        return currentTaskSet.isEmpty() && isEmpty();
    }

    @Override
    public void stop() {
        isStop = true;
    }


    @Override
    public void clear() {
        taskIdQueue.clear();
        taskMap.clear();
        currentTaskSet.clear();
    }

    // --------------------------------------------------
}
