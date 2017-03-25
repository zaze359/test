package com.zaze.aarrepo.commons.task.executor;


import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ZTag;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class AutoTaskExecutorService extends AsyncTaskExecutorService {

    public AutoTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
    }

    /**
     * 自动依次执行所有任务
     */
    public boolean autoExecute() {
        return executeAsyncTask();
//        if (taskExecutorService == null) {
//            return false;
//        }
//        if (autoExecutor == null) {
//            autoExecutor = Executors.newSingleThreadExecutor();
//            autoExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//        }
//        return true;
    }

    @Override
    protected void run() {
        try {
            do {
                if (taskExecutorService.isEmpty()) {
                    Thread.sleep(5000L);
                } else {
                    taskExecutorService.executeNextTask();
                    Thread.sleep(100L);
                }
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 中断取消所有任务
     */
    public void shutdown() {
        if (autoExecutor != null) {
            if (needLog) {
                ZLog.i(ZTag.TAG_TASK, "中断取消 自动执行任务池中的所有剩余任务!");
            }
            clear();
            autoExecutor.shutdown();
            autoExecutor = null;
        } else {
            ZLog.i(ZTag.TAG_TASK, "任务池不存在, 不需要中断!");
        }
    }
}
