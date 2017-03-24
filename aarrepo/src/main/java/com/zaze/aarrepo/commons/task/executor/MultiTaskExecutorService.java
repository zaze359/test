package com.zaze.aarrepo.commons.task.executor;


/**
 * Description : 多任务池服务
 *
 * @author zaze
 * @version 2017/3/25 - 上午12:25 1.0
 */
class MultiTaskExecutorService extends FilterTaskExecutorService {

    public MultiTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
    }

    /**
     * 执行下一批任务
     *
     * @param num 执行任务数
     * @return false
     */
    public boolean multiExecuteTask(int num) {
        boolean hasMore = false;
        for (int i = 0; i < num; i++) {
            hasMore = executeNextTask();
        }
        return hasMore;
    }
}
