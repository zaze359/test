package com.zaze.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/8/2 - 下午1:29 1.0
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                ZLog.i(ZTag.TAG, "收到系统启动广播");
            } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
                ZLog.i(ZTag.TAG, "收到系统关机广播");
            }
        }
    }
}
