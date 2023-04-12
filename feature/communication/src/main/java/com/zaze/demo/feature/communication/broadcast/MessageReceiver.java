package com.zaze.demo.feature.communication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.zaze.demo.feature.communication.parcel.IpcMessage;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-06 - 19:21
 */
public class MessageReceiver extends BroadcastReceiver {
    public static final String ACTION_MESSAGE = "com.zaze.action.message.testappid";
    public static final String ACTION_REPLAY = "com.zaze.action.message.replay";

    public static final String KEY_MESSAGE = "key_message";

    public static final String ID = "id";

    public static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MESSAGE);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ZLog.v(ZTag.TAG_DEBUG, "onReceive: " + action);
        if (TextUtils.equals(action, ACTION_MESSAGE)) {
//            int id = intent.getIntExtra(ID, -1);
            IpcMessage message = intent.getParcelableExtra(KEY_MESSAGE);
            ZLog.v(ZTag.TAG_DEBUG, "onReceive message : " + message);

            Intent replayIntent = new Intent(ACTION_REPLAY);
            message.setMessage("replay >> " + message.getMessage());
            replayIntent.putExtra(KEY_MESSAGE, message);
            context.sendBroadcast(replayIntent);
//            new Thread(new AlarmTask(context)).start();
        }

//        Intent serviceIntent = new Intent(context, NotificationService.class);
//        serviceIntent.putExtras(intent.getExtras());
//        context.startService(serviceIntent);
    }

    public void register(Context context) {
        context.registerReceiver(this, createIntentFilter());
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }
}
