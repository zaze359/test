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

    Messenger messenger = new Messenger(new Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ZLog.i(ZTag.TAG_DEBUG, "handleMessage");
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
