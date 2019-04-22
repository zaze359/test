package com.zaze.demo.app;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.zaze.common.base.BaseApplication;
import com.zaze.demo.component.network.compat.AnalyzeTrafficCompat;
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    private ClipboardManager mClipboardManager;

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

    }


}
