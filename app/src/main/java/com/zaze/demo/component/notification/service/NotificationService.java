package com.zaze.demo.component.notification.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.zaze.demo.R;
import com.zaze.demo.component.readpackage.ui.ReadPackageActivity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-03 - 17:25
 */
public class NotificationService extends Service {

    static int i = 0;

    @Nullable
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
        i++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ReadPackageActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        //设置小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知标题
        builder.setContentTitle("通知标题");
        //设置通知内容
        builder.setContentText("通知内容: " + i);
        builder.setTicker("Test Ticker");
        Notification notification = builder.build();
        startForeground(i, notification);
        // --------------------------------------------------
//        notification(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
