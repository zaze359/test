package com.zaze.demo.app;

import com.zaze.aarrepo.commons.cache.MemoryCache;
import com.zaze.aarrepo.utils.LocalDisplay;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 20:4
 */
public class MyApplication extends GpsApplication {
    //    BroadcastReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        LocalDisplay.init(this);
        MemoryCache.getInstance().setCacheLog(true);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }

}