package com.zaze.utils.task.executor;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.ExecuteTask;
import com.zaze.utils.task.TaskEmitter;
import com.zaze.utils.task.TaskEntity;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
public abstract class TaskPool {
    volatile boolean isStop = false;

    static volatile boolean needLog = false;

    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }

    /**
     * 执行任务
     *
     * @return true 执行成功, false 执行失败 （没有可任务时才会失败）
     */
    public abstract boolean executeTask(TaskEmitter emitter);

    /**
     * 添加任务
     *
     * @param entity TaskEntity
     * @param toHead 是否添加到头部
     */
    public abstract void pushTask(TaskEntity entity, boolean toHead);

    /**
     * 取一个任务
     *
     * @return ExecuteTask
     */
    abstract ExecuteTask pollTask();

    /**
     * 任务队列是否为空
     *
     * @return true 为空
     */
    public abstract boolean isEmpty();

    /**
     * 停止任务
     */
    void stop() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "暂停该任务池所有的后续任务!");
        }
        isStop = true;
    }

    /**
     * 清除任务
     */
    public void clear() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "清除该任务池所有任务!");
        }
    }

}
