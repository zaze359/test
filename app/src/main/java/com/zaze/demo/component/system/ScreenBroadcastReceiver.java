package com.zaze.demo.component.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-06 - 15:18
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            ZLog.i(ZTag.TAG_DEBUG, "开屏");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            ZLog.i(ZTag.TAG_DEBUG, "锁屏");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            ZLog.i(ZTag.TAG_DEBUG, "解锁");
        }
    }
}
