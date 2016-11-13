package com.zaze.app;

import android.app.Application;

import com.zz.library.util.LocalDisplay;

/**
 * Description :
 * @author : ZAZE
 * @version : 2016-11-06 - 20:43
 */
public class MyApplication extends Application {
//    BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        LocalDisplay.init(this);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.xh.message.testappid");
//        registerReceiver(receiver, intentFilter);
    }
}
