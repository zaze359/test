package com.zaze.aarrepo.commons.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.utils.StringUtil;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : 循环服务(处理一些需要循环调用的业务)
 * 运行原理 : 有注册和解绑业务(内部包括一些固定业务), 注册之后 会循环发送广播响应相应的业务处理
 * 任务类型 : 永驻任务(不可移除,暂时不需要),一般任务(不会自动解绑, 需手动解绑), 临时任务(1秒无再次调用后会解绑)
 * TODO 后期扩展 后台保活机制
 * TODO 后期扩展 针对同个类型里每个任务 有不同的 响应时间
 *
 * @author : ZAZE
 * @version : 2016-10-10 - 15:03
 */
public class TaskService extends Service {
    // --------------------------------
    /**
     * 事件池
     */
    private static final ConcurrentLinkedQueue<TaskEntity> actionPool = new ConcurrentLinkedQueue<TaskEntity>();
    // --------------------------------
    /**
     * 快速任务(最常用的任务, 自动解绑)
     */
    private static final ConcurrentHashMap<String, Long> fastActionMap = new ConcurrentHashMap<String, Long>();

    /**
     * 一般任务(不会自动解绑, 需手动解绑)
     */
    private static final ConcurrentHashMap<String, Long> ordinaryActionMap = new ConcurrentHashMap<String, Long>();

    /**
     * 常驻任务(一般为写死在内部)
     */
    private static final ConcurrentHashMap<String, Long> permanentActionMap = new ConcurrentHashMap<String, Long>();

    static { // 写死常驻任务
        permanentActionMap.put(TaskServiceAction.CHECK_XH_BOX, System.currentTimeMillis());
    }

    // --------------------------------
    private static long lastRunTimeFast = 0L;
    private static long lastRunTimeOrdinary = 0L;
    private static long lastRunTimePermanent = 0L;
    // --------------------------------
    private static final long loopTimeFast = 300L;
    private static final long loopTimeOrdinary = 5000L;
    private static final long loopTimePermanent = 1000L * 600;
    // --------------------------------
    private static boolean needLog = false;
    private static ExecutorService singleExecutor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                runTime();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void runTime() {
        while (true) {
            executeTask(System.currentTimeMillis());
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void executeTask(long currentRunTime) {
        notifyFastTask(currentRunTime);
        notifyOrdinaryTask(currentRunTime);
        notifyPermanentTask(currentRunTime);
    }

    /**
     * 广播通知临时业务
     *
     * @param currentRunTime
     */
    private void notifyFastTask(long currentRunTime) {
        if (currentRunTime - lastRunTimeFast > loopTimeFast) {
//            LogDevelopmentKit.i(StringUtil.format("RunTime(%d): notifyFastTask",
//                    currentRunTime
//            ));
            HashSet<String> actionSet = new HashSet<String>();
            while (!actionPool.isEmpty()) {
                TaskEntity taskEntity = actionPool.poll();
                actionSet.add(StringUtil.parseString(taskEntity.getAction()));
            }
            for (String action : actionSet) {
                LogKit.i("zaze 1011 sendBroadcast action( %s )", action);
                sendBroadcast(new Intent(action));
            }
            lastRunTimeFast = currentRunTime;
        }
    }

    /**
     * 广播通知一般业务
     *
     * @param currentRunTime
     */
    private void notifyOrdinaryTask(long currentRunTime) {
        if (currentRunTime - lastRunTimeOrdinary > loopTimeOrdinary) {
//            LogDevelopmentKit.i(StringUtil.format("RunTime(%d): notifyOrdinaryTask",
//                    currentRunTime
//            ));
            for (String key : ordinaryActionMap.keySet()) {
                sendBroadcast(new Intent(key));
            }
            lastRunTimeOrdinary = currentRunTime;
        }
    }

    /**
     * 广播通知常驻业务
     *
     * @param currentRunTime
     */
    private void notifyPermanentTask(long currentRunTime) {
        if (currentRunTime - lastRunTimePermanent > loopTimePermanent) {
            LogKit.i("RunTime(%d): notifyPermanentTask", currentRunTime);
            for (String key : permanentActionMap.keySet()) {
                sendBroadcast(new Intent(key));
            }
            lastRunTimePermanent = currentRunTime;
        }
    }
    // ------------------------------------------------

    /**
     * 注册一般业务
     *
     * @param action action
     */
    public static void registerOrdinaryTask(String action) {
        if (!StringUtil.stringIsNull(action)) {
            ordinaryActionMap.put(action, System.currentTimeMillis());
        }
    }

    /**
     * 解绑一般业务
     *
     * @param action action
     */
    public static void unRegisterOrdinaryTask(String action) {
        if (!StringUtil.stringIsNull(action)) {
            ordinaryActionMap.remove(action);
        }
    }
    // ------------------------------------------------

    /**
     * 注册临时业务
     *
     * @param action action
     */
    public static void registerFastTask(String action) {
        addTaskToPool(action);
    }

    private static void addTaskToPool(String action) {
        addTaskToPool(new TaskEntity(action, System.currentTimeMillis()));
    }

    private static void addTaskToPool(TaskEntity entity) {
//        LogDevelopmentKit.v("zaze 1011 addTaskToPool : " + entity);
        actionPool.add(entity);
    }

    // ------------------------------------------------
    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }
    // ------------------------------------------------

    private static class TaskEntity {
        private String action;
        private long time;

        public TaskEntity(String action, long time) {
            this.action = action;
            this.time = time;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "TaskEntity{" +
                    "action='" + action + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

}
