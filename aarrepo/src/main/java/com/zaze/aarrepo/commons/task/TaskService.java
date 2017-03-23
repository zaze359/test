package com.zaze.aarrepo.commons.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zaze.aarrepo.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : 循环服务(处理一些需要循环调用的业务)
 * 请求的发送 ： 使用 eventbus 和 广播 两种方式发送出事件
 * 运行原理 : 有注册和解绑业务(内部包括一些固定业务), 注册之后 会循环发送广播响应相应的业务处理
 * 任务类型 : 永驻任务(不可移除,暂时不需要),一般任务(不会自动解绑, 需手动解绑), 临时任务(1秒无再次调用后会解绑)
 * TODO 后期扩展 后台保活机制
 * TODO 后期扩展 针对同个类型里每个任务 有不同的 响应时间
 *
 * @author : ZAZE
 * @version : 2016-10-10 - 15:03
 */
public class TaskService extends Service {
    public static class TaskMode {
        public static final int BROADCAST = 1;
        public static final int BROADCAST_AND_EVENT_BUS = 2;
//        public static final int EVENT_BUS = 3;
    }

    private static int taskMode = TaskMode.BROADCAST_AND_EVENT_BUS;
    // --------------------------------
    /**
     * 事件池
     */
    private static final ConcurrentLinkedQueue<TaskEntity> actionPool = new ConcurrentLinkedQueue<>();
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
//        permanentActionMap.put(TaskServiceAction.RELEASE_MEMORY_CACHE, System.currentTimeMillis());
    }

    // --------------------------------
    private static long lastRunTimeFast = 0L;
    private static long lastRunTimeOrdinary = 0L;
    private static long lastRunTimePermanent = 0L;
    // --------------------------------
    private static long loopTimeFast = 300L;

    private static long loopTimeOrdinary = 1000L * 30;

    private static long loopTimePermanent = 1000L * 60 * 10;

    // --------------------------------
    private static boolean needLog = false;
    private static ExecutorService singleExecutor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (singleExecutor == null) {
            synchronized (ExecutorService.class) {
                if (singleExecutor == null) {
                    singleExecutor = Executors.newSingleThreadExecutor();
                    singleExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            runTime();
                        }
                    });
                }
            }
        }
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
            HashSet<String> actionSet = new HashSet<String>();
            while (!actionPool.isEmpty()) {
                TaskEntity taskEntity = actionPool.poll();
                actionSet.add(StringUtil.parseString(taskEntity.getAction()));
            }
            for (String action : actionSet) {
                sendMessage(action);
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
            for (String key : ordinaryActionMap.keySet()) {
                sendMessage(key);
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
            for (String key : permanentActionMap.keySet()) {
                sendMessage(key);
            }
            lastRunTimePermanent = currentRunTime;
        }
    }

    // ------------------------------------------------

    /**
     * 注册永驻业务（默认10分钟轮询一次）
     *
     * @param action action
     */
    public static void registerPermanentTask(String action) {
        if (!StringUtil.isEmpty(action)) {
            permanentActionMap.put(action, System.currentTimeMillis());
        }
    }

    // ------------------------------------------------

    /**
     * 注册一般业务（默认30秒轮询一次）
     *
     * @param action action
     */
    public static void registerOrdinaryTask(String action) {
        if (!StringUtil.isEmpty(action)) {
            ordinaryActionMap.put(action, System.currentTimeMillis());
        }
    }

    /**
     * 解绑一般业务
     *
     * @param action action
     */
    public static void unRegisterOrdinaryTask(String action) {
        if (!StringUtil.isEmpty(action)) {
            ordinaryActionMap.remove(action);
        }
    }
    // ------------------------------------------------

    /**
     * 注册临时业务（默认300毫秒轮询一次）
     *
     * @param action action
     */
    public static void registerFastTask(String action) {
        addTaskToPool(action);
    }

    private static void addTaskToPool(String action) {
        addTaskToPool(new TaskEntity(action).setExecuteTime(System.currentTimeMillis()));
    }

    private static void addTaskToPool(TaskEntity entity) {
        actionPool.add(entity);
    }

    //
    private void sendMessage(String action) {
        if (taskMode == TaskMode.BROADCAST_AND_EVENT_BUS) {
            EventBus.getDefault().post(new TaskEntity(action));
        }
        sendBroadcast(new Intent(action));

    }

    // ------------------------------------------------
    public static void setNeedLog(boolean isNeedLog) {
        needLog = isNeedLog;
    }

    public static void setTaskMode(int taskMode) {
        TaskService.taskMode = taskMode;
    }

    public static void setLoopTimeFast(long loopTimeFast) {
        TaskService.loopTimeFast = loopTimeFast;
    }

    public static void setLoopTimeOrdinary(long loopTimeOrdinary) {
        TaskService.loopTimeOrdinary = loopTimeOrdinary;
    }

    public static void setLoopTimePermanent(long loopTimePermanent) {
        TaskService.loopTimePermanent = loopTimePermanent;
    }

// ------------------------------------------------
}
