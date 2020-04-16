package com.zaze.common.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;

import com.zaze.common.thread.ThreadPlugins;
import com.zaze.utils.DisplayUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-07-15 - 14:32
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication instance;
    private int aliveActivityCount = 0;

    public static BaseApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        } else {
            return instance;
        }
    }

    private void initDisplay() {
        DisplayUtil.init(this, -1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ZLog.i(ZTag.TAG_DEBUG, "initDisplay");
        initDisplay();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ZLog.setNeedStack(true);
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                aliveActivityCount ++;
                ZLog.i(ZTag.TAG_DEBUG, "onActivityCreated aliveActivityCount: " + aliveActivityCount);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                aliveActivityCount --;
                if(aliveActivityCount == 0) {
                    // 此时没有活着的activity
                }
                ZLog.i(ZTag.TAG_DEBUG, "onActivityDestroyed aliveActivityCount: " + aliveActivityCount);
            }
        });
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
        initDisplay();
    }

    @Override
    public Resources getResources() {
//        ZLog.i(ZTag.TAG_DEBUG, "getResources");
        return DisplayUtil.replaceResource(super.getResources());
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
