package com.zaze.demo.app;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import com.zaze.common.base.BaseApplication;
import com.zaze.demo.component.network.compat.AnalyzeTrafficCompat;
import com.zaze.demo.receiver.PackageReceiver;
import com.zaze.demo.debug.wifi.WifiCompat;
import com.zaze.demo.debug.wifi.WifiJob;
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
    public ConnectivityManager.NetworkCallback networkCallback;

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
        if (isMainProcess()) {
            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (mClipboardManager != null) {
                mClipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        if (mClipboardManager.hasPrimaryClip()) {
                            ClipData clipData = mClipboardManager.getPrimaryClip();
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
                networkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        ZLog.i(ZTag.TAG_DEBUG, "onAvailable : " + network);
                        // 通wifiReceiver一样处理
                    }

                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        ZLog.e(ZTag.TAG_DEBUG, "onUnavailable ");
                    }

                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
                        ZLog.i(ZTag.TAG_DEBUG, "onLinkPropertiesChanged : " + network);

                    }

                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                        ZLog.i(ZTag.TAG_DEBUG, "onLosing : " + network + "   maxMsToLive : " + maxMsToLive);
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        ZLog.i(ZTag.TAG_DEBUG, "onLost : " + network);

                    }

                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                        ZLog.i(ZTag.TAG_DEBUG, "onCapabilitiesChanged : " + network);

                    }
                };
                WifiCompat.listenerByConn();
                startService(new Intent(this, WifiJob.class));
                WifiCompat.listenerByJob(this);
            }
        }
    }

}
