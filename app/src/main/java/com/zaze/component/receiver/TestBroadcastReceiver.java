package com.zaze.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zaze.component.service.MyService;
import com.zz.library.commons.log.LogKit;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 19:21
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogKit.v("zaze onReceive");
        Intent serviceIntent = new Intent(context, MyService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
//        notification(context);
    }


}
