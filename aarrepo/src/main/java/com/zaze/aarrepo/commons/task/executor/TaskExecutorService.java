package com.zaze.aarrepo.commons.task.executor;


import com.zaze.aarrepo.commons.task.ExecuteTask;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
abstract class TaskExecutorService implements ITaskExecutorService {
    protected static boolean needLog = false;

    /**
     * 执行下一个任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    public abstract boolean executeNextTask();

    /**
     * @param entity   TaskEntity
     * @param callback TaskCallback
     */
    public abstract void pushTask(TaskEntity entity, TaskCallback callback);

    /**
     * @return 取一个任务
     */
    public abstract ExecuteTask pollTask();

    /**
     * @return 任务队列是否为空
     */
    public abstract boolean isEmpty();

    /**
     * 清除任务
     */
    public abstract void clear();

    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }

}
