package com.zaze.demo.component.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zaze.demo.component.notification.service.NotificationService;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 19:21
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ZLog.v(ZTag.TAG_DEBUG, "zaze onReceive");
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }


}
