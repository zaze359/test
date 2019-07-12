package com.zaze.common.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Process;
import android.text.TextUtils;

import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.log.ZLog;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-07-15 - 14:32
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        } else {
            return instance;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ZDisplayUtil.init(this);
        ZLog.setNeedStack(false);
//        ZLog.setLogLevel(ZLogLevel.DEBUG);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MemoryCacheManager.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        MemoryCacheManager.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ZDisplayUtil.init(this);
    }

    public boolean isPortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 是否是主进程
     *
     * @return boolean
     */
    protected boolean isMainProcess() {
        String processName = null;
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == Process.myPid()) {
                    processName = process.processName;
                }
            }
        }
        return TextUtils.equals(getPackageName(), processName);
    }

}
