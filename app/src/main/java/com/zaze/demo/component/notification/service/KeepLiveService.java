package com.zaze.demo.component.notification.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.zaze.demo.R;
import com.zaze.demo.component.application.AppActivity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-03 - 17:25
 */
public class KeepLiveService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AppActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        //设置小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知标题
        builder.setContentTitle("通知标题");
        //设置通知内容
        builder.setContentText("通知内容");
        builder.setTicker("Test Ticker");
        Notification notification = builder.build();
        startForeground(0, notification);
        stopForeground(true);
        // --------------------------------------------------
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
