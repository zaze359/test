package com.zaze.utils.task;

import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.executor.SyncTaskPool;
import com.zaze.utils.task.executor.TaskPool;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : 添加移除任务
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 14:31
 */
public abstract class TaskPusher<T> extends Task<T> {

    private static final ConcurrentHashMap<String, TaskPool> POOL_MAP = new ConcurrentHashMap<>();

    public TaskPusher(String taskTag) {
        super(taskTag);
    }

    @Override
    protected Task<T> pushTask(TaskEntity entity, boolean pushToHead) {
        if (entity != null) {
            getTaskPool().pushTask(entity, pushToHead);
        }
        return this;
    }

    /**
     * 替换更新任务池
     *
     * @param executor executor
     */
    protected void putTaskPool(TaskPool executor) {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "更新任务池(%s)", poolTag);
        }
        POOL_MAP.put(poolTag, executor);
    }

    /**
     * 移除任务池
     */
    protected void removeTaskPool() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "移除任务池(%s)!", poolTag);
        }
        POOL_MAP.remove(poolTag);
    }

    protected TaskPool getTaskPool() {
        TaskPool taskPool = getTaskPool(poolTag);
        if (taskPool == null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "创建任务池(%s)", poolTag);
            }
            taskPool = new SyncTaskPool();
            putTaskPool(taskPool);
        }
        return taskPool;
    }

    /**
     * @return TaskPool
     */
    public static TaskPool getTaskPool(String poolTag) {
        if (hasTaskPool(poolTag)) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "提取任务池(%s)", poolTag);
            }
            return POOL_MAP.get(poolTag);
        } else {
            if (needLog) {
                ZLog.e(ZTag.TAG_TASK, "任务池(%s)不存在", poolTag);
            }
            return null;
        }
    }

    /**
     * 是否有指定任务池
     *
     * @param poolTag 任务tag
     * @return true 存在
     */
    private static boolean hasTaskPool(String poolTag) {
        return !TextUtils.isEmpty(poolTag) && POOL_MAP.containsKey(poolTag);
    }

    protected void clearPoll() {
        TaskPool taskPool = getTaskPool();
        taskPool.clear();
        taskPool = null;
        POOL_MAP.remove(poolTag);
    }

    public static void clearAllPoll() {
        Iterator iterator = POOL_MAP.keySet().iterator();
        while (iterator.hasNext()) {
            TaskPool taskPool = getTaskPool((String) iterator.next());
            if (taskPool != null) {
                taskPool.stop();
                taskPool.clear();
            }
        }
        POOL_MAP.clear();
    }
}
