package com.zaze.demo.app;

import com.tencent.bugly.crashreport.CrashReport;
import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.cache.MemoryCache;
import com.zaze.aarrepo.utils.LocalDisplay;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 20:4
 */
public class MyApplication extends ZBaseApplication {
    //    BroadcastReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        LocalDisplay.init(this);
        MemoryCache.getInstance().setCacheLog(true);
        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }

}
