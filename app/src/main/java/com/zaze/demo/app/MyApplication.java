package com.zaze.demo.app;


import com.tencent.bugly.crashreport.CrashReport;
import com.zaze.common.base.BaseApplication;
import com.zaze.demo.debug.AnalyzeTrafficCompat;
import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.cache.MemoryCacheManager;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 20:4
 */
public class MyApplication extends BaseApplication {
    //    BroadcastReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        ZDisplayUtil.init(this);
//        ZFileUtil.INSTANCE.setShowLog(true);
        MemoryCacheManager.setCacheLog(true);
        AnalyzeTrafficCompat.setNeedLog(true);
        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }

}
