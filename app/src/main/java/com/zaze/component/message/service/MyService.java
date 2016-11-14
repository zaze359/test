package com.zaze.component.message.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.zaze.R;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-03 - 17:25
 */
public class MyService extends Service {

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
        String title = intent.getStringExtra("title");
        title = "正在接收本地消息网关的消息";
        String content = intent.getStringExtra("content");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Test Ticker");
        builder.setContentTitle(title);
        builder.setContentText(content);
        Notification notification = builder.build();
        startForeground(i, notification);
        //
        notification(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void notification(Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(content);
        // 设置通知主题的意图
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
