package com.zaze.demo.component.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zaze.demo.component.webview.WebViewEvent;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.greenrobot.eventbus.EventBus;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-06 - 15:18
 */
public class ScreenLockReceiver extends BroadcastReceiver {
    public static void register(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(new ScreenLockReceiver(), intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            ZLog.i(ZTag.TAG_DEBUG, "开屏");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            ZLog.i(ZTag.TAG_DEBUG, "锁屏");
            EventBus.getDefault().post(new WebViewEvent());
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            ZLog.i(ZTag.TAG_DEBUG, "解锁");
        }
    }
}
