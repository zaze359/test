package com.zaze.utils.task.executor;

import android.support.annotation.NonNull;

import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.TaskCallback;
import com.zaze.utils.task.TaskEntity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : 任务池管理（标签分类）
 *
 * @author : ZAZE
 * @version : 2017-03-23 - 23:03
 */
public class TaskExecutorManager {
    private boolean needLog;
    private final ConcurrentHashMap<String, TaskExecutorService> executorMap = new ConcurrentHashMap<>();
    //    private final String AUTO_TASK_TAG = "$%#zaze_auto_#%$";
//    private final String MULTI_TASK_TAG = "$%#zaze_multi_#%$";
    private final String DEFAULT_TAG = "$%#zaze_default_#%$";
    private static volatile TaskExecutorManager instance;

    public static TaskExecutorManager getInstance() {
        if (instance == null) {
            synchronized (TaskExecutorManager.class) {
                if (instance == null) {
                    instance = new TaskExecutorManager();
                }
            }
        }
        return instance;
    }

    private TaskExecutorManager() {
    }
    // --------------------------------------------------

    /**
     * 添加任务到默认任务池
     *
     * @param entity   具体任务
     * @param callback 回调
     * @return TaskExecutorManager
     */
    public TaskExecutorManager pushTask(TaskEntity entity, TaskCallback callback) {
        return pushTask(DEFAULT_TAG, entity, callback);
    }

    /**
     * 添加任务
     *
     * @param tag      一类任务的标签（例如 : Download 表示 下载这一类任务）
     * @param entity   具体任务
     * @param callback 回调
     * @return TaskExecutorManager
     */
    public TaskExecutorManager pushTask(@NonNull String tag, TaskEntity entity, TaskCallback callback) {
        return pushTask(tag, entity, callback, false);
    }

    public TaskExecutorManager addFirst(TaskEntity entity, TaskCallback callback) {
        return addFirst(DEFAULT_TAG, entity, callback);
    }

    public TaskExecutorManager addFirst(@NonNull String tag, TaskEntity entity, TaskCallback callback) {
        return pushTask(tag, entity, callback, true);
    }
    // --------------------------------------------------

    /**
     * 添加任务
     *
     * @param tag      一类任务的标签（例如 : Download 表示 下载这一类任务）
     * @param entity   具体任务
     * @param callback 回调
     * @return TaskExecutorManager
     */
    private TaskExecutorManager pushTask(@NonNull String tag, TaskEntity entity, TaskCallback callback, boolean addFirst) {
        if (entity != null) {
            TaskExecutorService executorService = getTaskExecutorService(tag);
            if (executorService == null) {
                executorService = new SyncTaskExecutorService();
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "创建 标签(%s) 任务池", tag);
                }
            }
            executorMap.put(tag, executorService);
            if (addFirst) {
                executorService.addFirst(entity, callback);
            } else {
                executorService.pushTask(entity, callback);
            }
        }
        return this;
    }

    // --------------------------------------------------

    /**
     * 同步执行执行默认标签的 下一个任务(需要注意回调嵌套)
     */
    public void executeSyncNext() {
        for (String tag : executorMap.keySet()) {
            executeSyncNext(tag);
        }
    }

    /**
     * 同步执行执行指定标签的下一个任务(需要注意回调嵌套)
     *
     * @param tag 任务标签
     */
    public void executeSyncNext(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService != null) {
            if (!taskExecutorService.executeNextTask()) {
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "标签(%s)执行任务已经执行完毕, 移除标签！", tag);
                }
                executorMap.remove(tag);
            }
        }
    }
    // --------------------------------------------------

    /**
     * 异步执行默认标签的 下一个任务
     */
    public void executeAsyncNext() {
        for (String tag : executorMap.keySet()) {
            executeAsyncNext(tag);
        }
    }

    /**
     * 异步执行指定标签的下一个任务
     *
     * @param tag 任务标签
     */
    public void executeAsyncNext(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService != null) {
            if (!new AsyncTaskExecutorService(taskExecutorService).executeAsyncTask()) {
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "标签(%s)执行任务已经执行完毕, 移除标签！", tag);
                }
                executorMap.remove(tag);
            }
        }
    }
    // --------------------------------------------------

    /**
     * 多任务执行
     *
     * @param num 执行数
     */
    public void executeMulti(int num) {
        executeMulti(DEFAULT_TAG, num);
    }

    /**
     * 多任务执行
     *
     * @param tag 任务标签
     * @param num 执行数
     */
    public void executeMulti(String tag, int num) {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "执行 批量任务标签(%s)（%d）！", tag, num);
        }
        MultiTaskExecutorService multiTaskExecutorService = new MultiTaskExecutorService(getTaskExecutorService(tag));
        multiTaskExecutorService.multiExecuteTask(num, null);
    }
    // --------------------------------------------------

    /**
     * 自动执行 默认标签的任务
     */
    public void autoExecuteTask() {
        autoExecuteTask(DEFAULT_TAG);
    }

    /**
     * 自动执行指定标签的任务
     *
     * @param tag 标签
     */
    public void autoExecuteTask(String tag) {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "自动执行 标签(%s)内所有任务！", tag);
        }
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService instanceof AutoTaskExecutorService) {
            ((AutoTaskExecutorService) taskExecutorService).autoExecute();
        } else {
            AutoTaskExecutorService autoTaskExecutorService = new AutoTaskExecutorService(taskExecutorService);
//            executorMap.remove(tag);
            if (autoTaskExecutorService.autoExecute()) {
                executorMap.put(tag, autoTaskExecutorService);
            }
        }
    }

    /**
     * 终止默认标签中的后续任务
     */
    public void shutdownAutoExecuteTask() {
        shutdownAutoExecuteTask(DEFAULT_TAG);
    }

    /**
     * 终止后续任务
     */
    public void shutdownAutoExecuteTask(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService instanceof AutoTaskExecutorService) {
            ((AutoTaskExecutorService) taskExecutorService).shutdown();
        }

    }

    // --------------------------------------------------

    /**
     * 清除所有任务
     */
    public void clearAllTask() {
        for (String tag : executorMap.keySet()) {
            clearTaskByTag(tag);
        }
    }

    /**
     * 清除指定标签任务
     *
     * @param tag
     */
    public void clearTaskByTag(String tag) {
        TaskExecutorService taskExecutorService = getTaskExecutorService(tag);
        if (taskExecutorService != null) {
            taskExecutorService.clear();
        }
    }

    // --------------------------------------------------

    /**
     * @param tag 标签
     * @return TaskExecutorService
     */
    private TaskExecutorService getTaskExecutorService(String tag) {
        if (hasTaskExecutorService(tag)) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "提取 标签(%s) 任务池", tag);
            }
            return executorMap.get(tag);
        } else {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "标签(%s) 任务池不存在", tag);
            }
            return null;
        }
    }

    // --------------------------------------------------

    /**
     * 是否有指定任务池
     *
     * @param tag 任务tag
     * @return true 存在
     */
    public boolean hasTaskExecutorService(String tag) {
        return !ZStringUtil.isEmpty(tag) && executorMap.containsKey(tag);
    }

    // --------------------------------------------------

    /**
     * @param isNeedLog true 显示日志
     */
    public void setNeedLog(boolean isNeedLog) {
        this.needLog = isNeedLog;
        TaskExecutorService.setNeedLog(isNeedLog);
    }

}
