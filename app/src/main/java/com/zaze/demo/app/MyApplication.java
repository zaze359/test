package com.zaze.demo.app;


import com.tencent.bugly.crashreport.CrashReport;
import com.zaze.common.base.ZBaseApplication;
import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.ZFileUtil;
import com.zaze.utils.cache.MemoryCache;

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
        ZDisplayUtil.init(this);
        ZFileUtil.INSTANCE.setShowLog(true);
        MemoryCache.getInstance().setCacheLog(true);
        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }

}
