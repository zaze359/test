package com.zaze.aarrepo.commons.task.executor;


/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class AsyncTaskExecutorService extends FilterTaskExecutorService {

    public AsyncTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
    }

    @Override
    public boolean executeNextTask() {
        return super.executeNextTask();
    }

    // ------------------------------------------------
}
