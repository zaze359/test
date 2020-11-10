package com.zaze.demo.debug;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.lang.reflect.Method;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-04-25 - 15:31
 */
public class MessengerService extends Service {

    private static HandlerThread handlerThread = new HandlerThread("test_thread");

    static {
        handlerThread.start();
    }

    private TestBinder testBinder = new TestBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    Messenger messenger = new Messenger(new Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ZLog.i(ZTag.TAG_DEBUG, "handleMessage");
            try {
                Class<?> serviceManager = Class.forName("android.os.ServiceManager");
                Method method = serviceManager.getMethod("addService", String.class, IBinder.class);
                method.setAccessible(true);
                method.invoke(null, "testBinder", testBinder);
                ZLog.v(ZTag.TAG, "add testBinder to ServiceManager");
            } catch (Exception e) {
                ZLog.e(ZTag.TAG, "add testBinder to ServiceManager error");
                e.printStackTrace();
            }
            Message msgToClient = Message.obtain(msg);
            try {
                msg.replyTo.send(msgToClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
