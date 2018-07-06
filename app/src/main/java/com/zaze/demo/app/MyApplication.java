package com.zaze.demo.app;


import android.content.Context;

import com.zaze.common.base.BaseApplication;
import com.zaze.demo.debug.AnalyzeTrafficCompat;
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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        FileUtil.setShowLog(true);
        MemoryCacheManager.setCacheLog(true);
        AnalyzeTrafficCompat.setNeedLog(true);
        //
//        FontUtil.setDefaultFontFormSystem("DEFAULT", "Roboto-Light.ttf");
        //
//        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }


}
