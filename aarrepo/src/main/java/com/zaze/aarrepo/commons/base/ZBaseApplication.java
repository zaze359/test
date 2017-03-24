package com.zaze.aarrepo.commons.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zaze.aarrepo.commons.cache.MemoryCache;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.commons.task.service.TaskService;
import com.zaze.aarrepo.commons.task.service.TaskServiceAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-07-15 - 14:32
 */
public abstract class ZBaseApplication extends Application {
    private static ZBaseApplication instance;
    private BroadcastReceiver broadcastReceiver = new ReleaseMemoryCacheBroadcast();

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
        startService(new Intent(this, TaskService.class));
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(TaskServiceAction.RELEASE_MEMORY_CACHE);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MemoryCache.getInstance().clearMemoryCache();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(broadcastReceiver);
    }

    // ---------------------
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void releaseMemoryCache(TaskEntity entity) {
        dealAction(entity.getAction());
    }

    private class ReleaseMemoryCacheBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            dealAction(intent.getAction());
        }
    }
    // ---------------------

    private void dealAction(String action) {
        if (TaskServiceAction.RELEASE_MEMORY_CACHE.equalsIgnoreCase(action)) {
            MemoryCache.getInstance().onRelease();
        }
    }

}
