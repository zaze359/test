package com.zaze.aarrepo.commons.task.executor;

import android.support.annotation.NonNull;

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
public class TaskExecutorManager {
    private boolean needLog;
    private final ConcurrentHashMap<String, TaskExecutorService> executorMap = new ConcurrentHashMap<>();
    private final String AUTO_TASK_TAG = "$%#zaze_auto_#%$";
    private final String MULTI_TASK_TAG = "$%#zaze_multi_#%$";
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
        if (entity != null) {
            TaskExecutorService executorService = pollTaskExecutorService(tag);
            if (executorService == null) {
                executorService = new SingleTaskExecutorService();
                if (needLog) {
                    ZLog.i(ZTag.TAG_TASK, "创建 标签(%s) 任务池", tag);
                }
            }
            executorMap.put(tag, executorService);
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
        TaskExecutorService taskExecutorService = pollTaskExecutorService(tag);
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
     * 多任务执行
     *
     * @param tag 任务标签
     * @param num 执行数
     */
    public void executeMulti(String tag, int num) {
        ZLog.i(ZTag.TAG_TASK, "执行 批量任务标签(%s)（%d）！", tag, num);
        MultiTaskExecutorService multiTaskExecutorService = new MultiTaskExecutorService(pollTaskExecutorService(tag));
        if (!multiTaskExecutorService.multiExecuteTask(num)) {
            ZLog.i(ZTag.TAG_TASK, "移除标签%s！", tag);
            executorMap.remove(tag);
        } else {
            // 替换
            executorMap.put(tag, multiTaskExecutorService);
        }
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
        ZLog.i(ZTag.TAG_TASK, "自动执行 标签(%s)内所有任务！", tag);
        AutoTaskExecutorService autoTaskExecutorService = new AutoTaskExecutorService(pollTaskExecutorService(tag));
        executorMap.put(tag, autoTaskExecutorService);
        autoTaskExecutorService.autoExecute();
//        if (!) {
//            ZLog.i(ZTag.TAG_TASK, "没有移除标签%s！", tag);
//            executorMap.remove(tag);
//        }
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
        TaskExecutorService taskExecutorService = pollTaskExecutorService(tag);
        if (taskExecutorService instanceof AutoTaskExecutorService) {
            ((AutoTaskExecutorService) taskExecutorService).shutdown();
        }

    }
    // --------------------------------------------------

    /**
     * @param tag 标签
     * @return TaskExecutorService
     */
    private TaskExecutorService pollTaskExecutorService(String tag) {
        if (!StringUtil.isEmpty(tag) && executorMap.containsKey(tag)) {
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

    /**
     * @param isNeedLog true 显示日志
     */
    public void setNeedLog(boolean isNeedLog) {
        this.needLog = isNeedLog;
        TaskExecutorService.setNeedLog(isNeedLog);
    }

}
