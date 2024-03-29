package com.zaze.utils.task;

import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.executor.AutoTaskPool;
import com.zaze.utils.task.executor.TaskPool;

/**
 * Description : 异步自动顺序执行指定标签的任务
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:14
 */
public class TaskAsyncAuto<T> extends TaskCreate<T> {
    TaskAsyncAuto(TaskCreate<T> create) {
        super(create.poolTag);
    }

    @Override
    protected void executeActual() {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("开始异步自动依序执行任务池(%s)内所有任务！", poolTag));
        }
        TaskPool taskPool = getOrCreatePool();
        if (taskPool instanceof AutoTaskPool) {
            executeTask(taskPool, false);
        } else {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, ZStringUtil.format("转换为异步自动依序执行模式(%s)", poolTag));
            }
            executeTask(AutoTaskPool.newInstance(taskPool), true);
        }
    }
}
