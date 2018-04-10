package com.zaze.common.base;

import android.app.Application;
import android.content.res.Configuration;

import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.ZSharedPrefUtil;
import com.zaze.utils.cache.MemoryCacheManager;


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
        ZSharedPrefUtil.initSharedPreferences(this);
        ZDisplayUtil.init(this);
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
}
