package com.zaze.demo.component.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 19:21
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION = "android.intent.action.xh.message.testappid";
    public static final String TEST = "test";
    public static final String ID = "id";

    @Override
    public void onReceive(Context context, Intent intent) {
        ZLog.v(ZTag.TAG_DEBUG, "zaze onReceive");
        try {
            if (TextUtils.equals(intent.getAction(), ACTION)) {
                int id = intent.getIntExtra(ID, -1);
                Bean bean = intent.getParcelableExtra(TEST);
                ZLog.v(ZTag.TAG_DEBUG, "zaze onReceive id : " + id);
                ZLog.v(ZTag.TAG_DEBUG, "zaze onReceive bean : " + bean.getId());
//                ZLog.v(ZTag.TAG_DEBUG, "zaze onReceive bean : " + bean);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

//        Intent serviceIntent = new Intent(context, NotificationService.class);
//        serviceIntent.putExtras(intent.getExtras());
//        context.startService(serviceIntent);
    }


}
