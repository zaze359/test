package com.zaze.demo.feature.communication.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.zaze.common.base.LogService;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-04-25 - 15:31
 */
public class MessengerService extends LogService {

    private static HandlerThread handlerThread = new HandlerThread("test_thread");

    private static String TAG = ZTag.TAG + "IPC_Messenger";

    @Override
    public boolean getShowLifecycle() {
        return true;
    }

    static {
        handlerThread.start();
    }

//    private TestBinder testBinder = new TestBinder();


    Messenger messenger = new Messenger(new Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String content = data.getString("content");
            ZLog.i(TAG, "handleMessage: " + content);
//            try {
//                Class<?> serviceManager = Class.forName("android.os.ServiceManager");
//                Method method = serviceManager.getMethod("addService", String.class, IBinder.class);
//                method.setAccessible(true);
//                method.invoke(null, "testBinder", testBinder);
//                ZLog.v(ZTag.TAG, "add testBinder to ServiceManager");
//            } catch (Exception e) {
//                ZLog.e(ZTag.TAG, "add testBinder to ServiceManager error");
//                e.printStackTrace();
//            }
            Message msgToClient = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("replay", content + " is reached");
            msgToClient.setData(bundle);
            try {
                msg.replyTo.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(@Nullable Intent intent) {
        super.onBind(intent);
        return messenger.getBinder();
    }
}
