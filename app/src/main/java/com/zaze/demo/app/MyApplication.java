package com.zaze.demo.app;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.zaze.common.base.BaseApplication;
import com.zaze.common.thread.ThreadPlugins;
import com.zaze.demo.component.network.compat.AnalyzeTrafficCompat;
import com.zaze.demo.debug.DefaultNetworkCallback;
import com.zaze.demo.debug.LogcatService;
import com.zaze.demo.debug.wifi.WifiCompat;
import com.zaze.demo.receiver.PackageReceiver;
import com.zaze.utils.NetUtil;
import com.zaze.utils.ZCommand;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 20:4
 */
public class MyApplication extends BaseApplication {
    //    BroadcastReceiver receiver;

    public static MyApplication getInstance() {
        return (MyApplication) BaseApplication.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    private ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ZCommand.setShowLog(true);
//        FileUtil.setShowLog(true);
        ThreadPlugins.runInUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    startService(new Intent(getInstance(), LogcatService.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10_000L);
        MemoryCacheManager.setCacheLog(true);
        AnalyzeTrafficCompat.setNeedLog(true);
//        CrashReport.initCrashReport(getApplicationContext(), "ecf90d7662", true);
        //
//        FontUtil.setDefaultFontFormSystem("DEFAULT", "Roboto-Light.ttf");
        //
//        CrashReport.initCrashReport(getApplicationContext(), "900013682", false);
//        receiver = new TestBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter("android.intent.action.message.testappid");
//        registerReceiver(receiver, intentFilter);
        if (isMainProcess()) {
            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (mClipboardManager != null) {
                mClipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        if (mClipboardManager.hasPrimaryClip()) {
                            ClipData clipData = mClipboardManager.getPrimaryClip();
                            if (clipData == null) {
                                return;
                            }
                            int count = clipData.getItemCount();
                            if (count > 0) {
                                ClipData.Item item = clipData.getItemAt(0);
                                ZLog.i(ZTag.TAG_DEBUG, "clipData : " + item.getText());
                            }
                        }
                    }
                });
            }
            PackageReceiver broadcastReceiver = new PackageReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            registerReceiver(broadcastReceiver, intentFilter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                WifiCompat.listenerByConn(new DefaultNetworkCallback(NetUtil.getConnectivityManager(this)));
            } else {
                WifiCompat.listenerByBroadcast(this);
            }
        }
    }

}
