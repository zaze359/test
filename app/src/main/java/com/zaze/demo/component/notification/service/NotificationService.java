package com.zaze.demo.component.notification.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.zaze.demo.R;
import com.zaze.demo.component.application.AppListActivity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-03 - 17:25
 */
public class NotificationService extends Service {
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "aaa");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AppListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("通知标题")
                .setContentIntent(contentIntent)
                .setContentText("通知内容")
                .setTicker("Test Ticker")
                .setAutoCancel(true)
//                .setOngoing(true)
                .setDefaults(~NotificationCompat.DEFAULT_SOUND ^ NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notification = builder.build();
        startForeground(2, notification);
        // --------------------------------------------------
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
