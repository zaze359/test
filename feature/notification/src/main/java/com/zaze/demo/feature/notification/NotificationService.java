package com.zaze.demo.feature.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.zaze.common.base.LogService;
import com.zaze.utils.AppUtil;
import com.zaze.utils.BmpUtil;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-03 - 17:25
 */
public class NotificationService extends LogService {
    String channelId = "NotificationService111";
    RemoteViews remoteViewsBig;
    RemoteViews remoteViews;
    NotificationCompat.Builder builder;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
                notificationChannel.setShowBadge(false);
                NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel);
            }
        }
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.music_notification_layout);
        remoteViewsBig = new RemoteViews(this.getPackageName(), R.layout.music_notification_expend_layout);
        builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_message)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // 未展开
                .setCustomContentView(remoteViews)
                // 展开时
                .setCustomBigContentView(remoteViewsBig);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification();
        return START_STICKY;
    }

    private void notification() {
//        builder.setContentTitle("通知标题")
//                .setContentText("通知内容")
//                .setTicker("Test Ticker")
//                .setOngoing(true)
//                .setDefaults(~NotificationCompat.DEFAULT_SOUND ^ NotificationCompat.DEFAULT_VIBRATE)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
       //            remoteViews.setOnClickPendingIntent(R.id.notification_play_btn, buildPendingIntent);

        bindView(remoteViews);
        bindView(remoteViewsBig);
        startForeground(11111, builder.build());
    }

    private void bindView(RemoteViews remoteViews) {
        remoteViews.setImageViewBitmap(R.id.music_notification_iv, BmpUtil.drawable2Bitmap(AppUtil.getAppIcon(this)));
        remoteViews.setTextViewText(R.id.music_notification_name_tv, "title");
        remoteViews.setTextViewText(R.id.music_notification_artist_tv, "artistName");
        remoteViews.setImageViewResource(R.id.music_notification_play_btn, R.drawable.music_play_circle_outline_black_24dp);
//            remoteViews.setOnClickPendingIntent(R.id.notification_next_btn, buildPendingIntent);
//            remoteViews.setOnClickPendingIntent(R.id.notification_close_btn, buildPendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
