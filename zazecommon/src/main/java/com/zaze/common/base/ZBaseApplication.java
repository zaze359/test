package com.zaze.common.base;

import android.app.Application;

import com.zaze.utils.cache.MemoryCache;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-07-15 - 14:32
 */
public abstract class ZBaseApplication extends Application {
    private static ZBaseApplication instance;

    public static ZBaseApplication getInstance() {
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
//        startService(new Intent(this, TaskService.class));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MemoryCache.getInstance().clearMemoryCache();
    }
}
