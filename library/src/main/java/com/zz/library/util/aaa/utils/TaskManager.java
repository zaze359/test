package com.zz.library.util.aaa.utils;


import com.zaze.aarrepo.commons.log.LogKit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Description	: 任务管理类(乱,要优化)
 *
 * @author : zaze
 * @version : 2015年6月2日 上午9:30:04
 */
public class TaskManager implements TaskBack {
    private LinkedList<TaskBean> waitTaskQueue;// 等待任务队列
    private ArrayList<TaskBean> executeTaskQueue;// 执行中的任务队列
    private LinkedList<TaskBean> failedTaskQueue;// 失败任务队列

    /**
     * 并发执行的任务数
     */
    private final int executeMaxSize = 5;

    private static volatile TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    private TaskManager() {
        super();
        waitTaskQueue = new LinkedList<>();
        failedTaskQueue = new LinkedList<>();
        executeTaskQueue = new ArrayList<>();
    }

    /**
     * Description	: 放入任务队列
     * @param task
     * @author        : zaze
     * @version    : 2015年5月28日 上午10:30:24
     */
//	public void offerIntervalTask(TaskBean taskBean) {
//		taskQueue.add(taskBean);
//	}

//	public void removeInstantTask(TaskBean taskBean) {
//		taskQueue.remove(taskBean);
//	}

    /**
     * Description	: 准备执行任务
     * @param task
     * @author        : zaze
     * @version    : 2015年5月28日 上午10:30:24
     */
//	public void prepareExcuteTask(TaskBean taskBean) {
//		executeTaskQueue.add(taskBean);
//	}

    /**
     * Description	: 放入到队列中
     *
     * @param taskBean
     * @return : true 可以执行, false 等待队列中
     * @author : zaze
     * @version : 2015年6月2日 上午11:51:26
     */
    public boolean putToTaskQueue(TaskBean taskBean) {
        executeTaskQueue.add(taskBean);
        return true;
//		if(executeTaskQueue.size() < executeMaxSize) {
//			executeTaskQueue.add(taskBean);
//			return true;
//		} else {
//			// 若执行队列已满 放入到 等待队列中
//			taskBean.setStatus(0);
//			waitTaskQueue.add(taskBean);
//			return false;
//		}
    }

    /**
     * Description	: 放入到队列中
     *
     * @param taskBeans
     * @return : true 可以执行, false 等待队列中
     * @author : zaze
     * @version : 2015年6月2日 上午11:51:26
     */
    public boolean putToTaskQueue(ArrayList<TaskBean> taskBeans) {
        executeTaskQueue.addAll(taskBeans);
        return true;
//		if(executeTaskQueue.size() < executeMaxSize) {
//			executeTaskQueue.add(taskBean);
//			return true;
//		} else {
//			// 若执行队列已满 放入到 等待队列中
//			taskBean.setStatus(0);
//			waitTaskQueue.add(taskBean);
//			return false;
//		}
    }


    /**
     * Description	: 根据优先级对等待队列进行排序
     *
     * @author : zaze
     * @version : 2015年6月2日 上午10:04:11
     */
    private void sortByPriority() {
    }

    /**
     * Description	: 执行任务
     *
     * @author : zaze
     * @version : 2015年5月28日 上午11:12:09
     */
    public void excuteIntervalTask() {
        //
        sortByPriority();
        int temp = executeMaxSize - executeTaskQueue.size();
        if (temp > 0) {
            if (waitTaskQueue.size() <= temp) {
                executeTaskQueue.addAll(waitTaskQueue);
            } else {
                List<TaskBean> list = waitTaskQueue.subList(0, temp);
                executeTaskQueue.addAll(list);
                waitTaskQueue.removeAll(list);
            }
        }
        for (int i = 0; i < executeTaskQueue.size(); i++) {
            TaskBean taskBean = executeTaskQueue.get(i);
            if (taskBean.getStatus() == 0) {
                taskBean.setStatus(1);
                taskBean.executeTask();
            } else if (taskBean.getStatus() == 2) {
                executeTaskQueue.remove(taskBean);
            }
        }
    }

    /**
     * Description	: 执行多任务
     *
     * @author : zaze
     * @version : 2015年5月28日 上午11:12:09
     */
    public void excuteMultiIntervalTasks(LinkedList<TaskBean> taskBeans) {
        // 临时， 后面带优化， 结合线程管理 多任务并发执行？
        int size = taskBeans.size();
        for (int i = 0; i < size; i++) {
            TaskBean taskBean = taskBeans.get(i);
            taskBean.setStatus(1);
            taskBean.executeTask();
        }
    }

    @Override
    public void excuteSuccess(String method, Object result, Object code) {
        int size = executeTaskQueue.size();
        LogKit.i(size + " excuteSuccess method : " + method);
        LogKit.v(size + " excuteSuccess result : " + result);
        LogKit.v(size + " excuteSuccess code : " + code);
        for (int i = 0; i < size; i++) {
            TaskBean taskBean = executeTaskQueue.get(i);
            if (method.equals(taskBean.getFunctionName())) {
                taskBean.setStatus(2);
                executeTaskQueue.remove(taskBean);
                size--;
                taskBean.excuteSuccess(method, result, code);
            }
        }
    }

    @Override
    public void excuteFailed(String method, Object msg, Object code) {
        LogKit.i("excuteFailed method : " + method);
        int size = executeTaskQueue.size();
        for (int i = 0; i < size; i++) {
            TaskBean taskBean = executeTaskQueue.get(i);
            if (method.equals(taskBean.getFunctionName())) {
                executeTaskQueue.remove(taskBean);
                size--;
                taskBean.setStatus(0);
                failedTaskQueue.add(taskBean);
                taskBean.excuteFailed(method, msg, code);
            }
        }
    }


}
