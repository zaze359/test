package com.zaze.utils.task.executor;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description  : 任务池服务
 * taskIdQueue  : 任务id队列
 * taskMap      : 任务map, 相同的taskId 只存在一个任务
 *
 * @author : ZAZE
 * @version : 2016-12-14 - 10:26
 */
class AutoTaskExecutorService extends AsyncTaskExecutorService {
    private boolean isRunning = false;

    public AutoTaskExecutorService(TaskExecutorService taskExecutorService) {
        super(taskExecutorService);
    }

    @Override
    public boolean executeAsyncTask() {
        try {
            if (taskExecutorService == null) {
                return false;
            } else {
                if (!isRunning) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (needLog) {
                                ZLog.d(ZTag.TAG_TASK, "run start");
                            }
                            isRunning = true;
                            try {
                                while (!taskExecutorService.isEmpty()) {
                                    running();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isRunning = false;
                            if (needLog) {
                                ZLog.d(ZTag.TAG_TASK, "run end");
                            }
                        }
                    });
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void clear() {
        super.clear();
        if (needLog) {
            ZLog.i(ZTag.TAG_TASK, "中断取消 自动执行任务池中的所有剩余任务!");
        }
        isRunning = false;
        executorService.shutdown();
    }
}
