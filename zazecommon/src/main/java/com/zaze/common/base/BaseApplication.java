package com.zaze.common.base;

import android.app.Application;

import com.zaze.utils.ZSharedPrefUtil;
import com.zaze.utils.cache.MemoryCache;


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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MemoryCache.getInstance().onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        MemoryCache.getInstance().onTrimMemory(level);
    }
}
