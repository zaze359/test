package com.zaze.demo.debug;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.log.LogcatUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-06-26 - 14:02
 */
public class LogcatService extends Service {

    private static ExecutorService looperExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        if (looperExecutor == null) {
            looperExecutor = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r, "LogcatService");
                    if (thread.isDaemon()) {
                        thread.setDaemon(false);
                    }
                    return thread;
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (looperExecutor != null) {
            looperExecutor.shutdownNow();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            ZLog.i(ZTag.TAG_DEBUG, action);
            if ("com.xuehai.logcat.service.start".equals(action)) {
                if (!LogcatUtil.isRunning()) {
                    looperExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogcatUtil.startCatchLog(MyApplication.getInstance());
                        }
                    });
                }
            } else if ("com.xuehai.logcat.service.stop".equals(action)) {
                LogcatUtil.stopCatchLog();
            }


        }
        return super.onStartCommand(intent, flags, startId);
    }


}
