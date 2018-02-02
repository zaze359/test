package com.zaze.utils.task;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.executor.MultiTaskPool;
import com.zaze.utils.task.executor.TaskPool;

/**
 * Description : 异步多任务执行
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:14
 */
class TaskAsyncMulti<T> extends TaskCreate<T> {
    TaskAsyncMulti(TaskCreate<T> create) {
        super(create.poolTag);
    }

    @Override
    protected void executeActual(Executor<TaskEntity> executor) {
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "开始批量执行任务池(%s)内任务！", poolTag);
        }
        TaskPool taskPool = getTaskPool();
        boolean replace;
        MultiTaskPool multiTaskExecutorService;
        if (taskPool instanceof MultiTaskPool) {
            multiTaskExecutorService = (MultiTaskPool) taskPool;
            replace = false;
        } else {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "转换为异步多任务执行模式(%s)", poolTag);
            }
            multiTaskExecutorService = MultiTaskPool.newInstance(taskPool);
            replace = true;
        }
//        if (multiTaskExecutorService != null) {
//            multiTaskExecutorService.setMultiNum(multiNum);
//        }
        executeTask(multiTaskExecutorService, replace, executor);
    }
}
