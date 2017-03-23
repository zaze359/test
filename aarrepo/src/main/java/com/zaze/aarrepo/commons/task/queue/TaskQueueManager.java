package com.zaze.aarrepo.commons.task.queue;

import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : 任务池管理（标签分类）
 *
 * @author : ZAZE
 * @version : 2017-03-23 - 23:03
 */
public class TaskQueueManager {
    private boolean isNeedLog;
    private final ConcurrentHashMap<String, TaskExecutorService> executorMap = new ConcurrentHashMap<>();
    private final String AUTO_TASK_TAG = "com.zaze.task.auto";
    private static volatile TaskQueueManager instance;

    public static TaskQueueManager getInstance() {
        if (instance == null) {
            synchronized (TaskQueueManager.class) {
                if (instance == null) {
                    instance = new TaskQueueManager();
                }
            }
        }
        return instance;
    }

    private TaskQueueManager() {
    }

    /**
     * 添加任务
     *
     * @param tag      一类任务的标签（例如 : Download 表示 下载这一类任务）
     * @param entity   具体任务
     * @param callback 回调
     * @return TaskQueueManager
     */
    public TaskQueueManager pushTask(String tag, TaskEntity entity, TaskCallback callback) {
        if (entity != null) {
            TaskExecutorService executorService = getTaskExecutorService(tag);
            if (executorService == null) {
                if (isNeedLog) {
                    ZLog.i(ZTag.TAG_TASK, "创建 标签(%s) 任务池", tag);
                }
                executorService = new TaskExecutorService();
                executorMap.put(tag, executorService);
            }
            executorService.pushTask(entity, callback);
        }
        return this;
    }

    /**
     * 执行所有标签的 下一个任务
     */
    public void executeNext() {
        for (String tag : executorMap.keySet()) {
            executeNext(tag);
        }
    }

    /**
     * 执行指定标签的下一个任务
     *
     * @param tag 任务标签
     */
    public void executeNext(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService != null) {
            if (!taskExecutorService.executeNextTask()) {
                if (isNeedLog) {
                    ZLog.i(ZTag.TAG_TASK, "标签(%s)执行任务已经执行完毕, 移除标签！", tag);
                }
                executorMap.remove(tag);
            }
        }
    }


    /**
     * 添加自动执行的任务
     *
     * @param entity
     * @param callback
     */
    public void pushAutoTask(TaskEntity entity, TaskCallback callback) {
        pushTask(AUTO_TASK_TAG, entity, callback);
    }

    /**
     * 自动执行指定标签
     */
    public void autoExecuteTask() {
        TaskExecutorService taskExecutorService = getTaskExecutorService(AUTO_TASK_TAG);
        if (taskExecutorService != null) {
            taskExecutorService.autoExecuteTask();
        }
    }

    /**
     * 终止后续任务
     */
    public void shutdown() {
        TaskExecutorService taskExecutorService = getTaskExecutorService(AUTO_TASK_TAG);
        if (taskExecutorService != null) {
            taskExecutorService.shutdown();
        }
    }

    /**
     * 自动执行指定标签的 所有任务
     *
     * @param tag 标签
     */
    private void autoExecuteTask(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService != null) {
            taskExecutorService.autoExecuteTask();
        }
    }

    /**
     * 自动执行 所有标签的 所有任务
     */
//    public void autoExecuteAll() {
//        for (String tag : executorMap.keySet()) {
//            autoExecuteTask(tag);
//        }
//    }


    // --------------------------------------------------

    /**
     * @param tag 标签
     * @return TaskExecutorService
     */
    private TaskExecutorService getTaskExecutorService(String tag) {
        if (!StringUtil.isEmpty(tag) && executorMap.containsKey(tag)) {
            if (isNeedLog) {
                ZLog.i(ZTag.TAG_TASK, "提取 标签(%s) 任务池", tag);
            }
            return executorMap.get(tag);
        } else {
            if (isNeedLog) {
                ZLog.i(ZTag.TAG_TASK, "标签(%s) 任务池不存在", tag);
            }
            return null;
        }
    }

    /**
     * 是否显示日志
     *
     * @param isNeedLog
     */
    public void isNeedLog(boolean isNeedLog) {
        this.isNeedLog = isNeedLog;
        TaskExecutorService.setNeedLog(isNeedLog);
    }

}
