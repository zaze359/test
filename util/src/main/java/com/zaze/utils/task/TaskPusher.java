package com.zaze.utils.task;

import android.text.TextUtils;

import com.zaze.utils.ZStringUtil;
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
    public Task<T> pushTask(TaskEntity entity, boolean pushToHead) {
        if (entity != null) {
            getOrCreatePool().pushTask(entity, pushToHead);
        }
        return this;
    }

    @Override
    public Task<T> removeTask(String taskId) {
        if (!TextUtils.isEmpty(taskId)) {
            getOrCreatePool().removeTask(taskId);
        }
        return this;
    }

    /**
     * 替换更新任务池
     *
     * @param executor executor
     */
    protected void putPool(TaskPool executor) {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("更新任务池(%s)", poolTag));
        }
        POOL_MAP.put(poolTag, executor);
    }

    /**
     * 移除任务池
     */
    protected void removePool() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("移除任务池(%s)!", poolTag));
        }
        POOL_MAP.remove(poolTag);
    }

    protected TaskPool getOrCreatePool() {
        TaskPool taskPool = getPool(poolTag);
        if (taskPool == null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("创建任务池(%s)", poolTag));
            }
            taskPool = new SyncTaskPool();
            putPool(taskPool);
        }
        return taskPool;
    }

    /**
     * @return TaskPool
     */
    public static TaskPool getPool(String poolTag) {
        if (hasPool(poolTag)) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("提取任务池(%s)", poolTag));
            }
            return POOL_MAP.get(poolTag);
        } else {
            if (needLog) {
                ZLog.e(ZTag.TAG_TASK, ZStringUtil.format("任务池(%s)不存在", poolTag));
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
    private static boolean hasPool(String poolTag) {
        return !TextUtils.isEmpty(poolTag) && POOL_MAP.containsKey(poolTag);
    }


    @Override
    public void clearPoll() {
        TaskPool taskPool = getPool(poolTag);
        if (taskPool != null) {
            taskPool.clear();
        }
        POOL_MAP.remove(poolTag);
    }

    @Override
    public boolean isEmpty() {
        TaskPool taskPool = getPool(poolTag);
        return taskPool == null || taskPool.isEmpty();
    }

    @Override
    public boolean isIdle() {
        TaskPool taskPool = getPool(poolTag);
        return taskPool == null || taskPool.isIdle();
    }

    protected static void clearAllPoll() {
        Iterator iterator = POOL_MAP.keySet().iterator();
        while (iterator.hasNext()) {
            TaskPool taskPool = getPool((String) iterator.next());
            if (taskPool != null) {
                taskPool.stop();
                taskPool.clear();
            }
        }
        POOL_MAP.clear();
    }

}
