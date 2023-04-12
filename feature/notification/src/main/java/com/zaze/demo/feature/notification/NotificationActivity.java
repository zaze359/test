package com.zaze.demo.feature.notification;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.zaze.common.base.AbsActivity;
import com.zaze.utils.AppUtil;
import com.zaze.utils.BmpUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-08 03:56 1.0
 */
public class NotificationActivity extends AbsActivity {
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @NonNull
    @Override
    public String[] getPermissionsToRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.POST_NOTIFICATIONS};
        } else {
            return super.getPermissionsToRequest();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        final ContextWrapper context = new ContextWrapper(this) {
//            @Override
//            public String getPackageName() {
//                return "android";
//            }
        };
        findViewById(R.id.notification_1_btn).setOnClickListener(v -> {
            startService(new Intent(context, NotificationService.class));
        });
        findViewById(R.id.notification_2_btn).setOnClickListener(v -> {
            notification(context, "channelId_2", 11);
        });
        findViewById(R.id.notification_3_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager mNotificationManager = (NotificationManager) NotificationActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();
                stopService(new Intent(NotificationActivity.this, NotificationService.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void notification(Context context, String channelId, int notifyId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle("title")
                .setContentText("content")
                .setDefaults(~Notification.DEFAULT_SOUND)
                .setLargeIcon(BmpUtil.drawable2Bitmap(AppUtil.getAppIcon(context)))
                .setAutoCancel(true);
        // style
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.notification_big_img)));
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfa" +
//                "sdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasd" +
//                "fasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf")
//        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 若之前没有设置声音和震动，后面即使设置了可能也无法正常震动和响铃，可以修改一下channelId 处理。
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            // 数组依次表示 静止时长，震动时长。这样循环
//            notificationChannel.setVibrationPattern(new long[]{2000, 2000, 2000, 2000});
            // LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setSound();
            notificationChannel.setShowBadge(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel);

        } else {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000})
                    // 声音和震动
//                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                    .setSound(Uri.fromFile(new File(xxxx)))
                    .setLights(Color.RED, 2000, 2000)
                    // LOW MIN 时静默
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        //
        PendingIntent contentIntent;
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag |= PendingIntent.FLAG_IMMUTABLE;
        }
        contentIntent = PendingIntent.getActivity(context, 0,
                new Intent("com.zaze.demo.action.ENTRANCE"), flag);
        builder.setContentIntent(contentIntent);
        //
        notification(context, builder, notifyId);
    }
    private void notification(Context context, NotificationCompat.Builder builder, int notifyId) {
        builder.setSmallIcon(R.drawable.baseline_message_24);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ZLog.w(ZTag.TAG_ERROR, "need permission: Manifest.permission.POST_NOTIFICATIONS");
            return;
        }
        NotificationManagerCompat.from(context).notify(notifyId, builder.build());
    }


    private void notification2() {
        final ContextWrapper context = new ContextWrapper(this) {
//            @Override
//            public String getPackageName() {
//                return "android";
//            }
        };
//        final Context contextWrapper = this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            UserHandle.getUserHandleForUid(Process.SYSTEM_UID);
//        }
//        //
////        Method userHolder = null;
        try {
////            userHolder = ContextWrapper.class.getDeclaredMethod("getUser");
////            userHolder.setAccessible(true);
//            Object proxyContext = Proxy.newProxyInstance(contextWrapper.getClass().getClassLoader(), new
//                    Class[]{Context.class}, new InvocationHandler() {
//                @Override
//                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                    String name = method.getName();
//                    Log.d("", "invoke(). method:" + name);
//                    return method.invoke(contextWrapper, args);
//                }
//            });
//            Context context = (Context) proxyContext;
            //

            String channelId = "Notification";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW);
                // 数组依次表示 静止时长，震动时长。这样循环
                notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
                NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel);
            }
            PendingIntent contentIntent;
            int flag = PendingIntent.FLAG_UPDATE_CURRENT;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flag |= PendingIntent.FLAG_IMMUTABLE;
            }
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent("com.zaze.demo.action.ENTRANCE"), flag);
            // --------------------------------------------------
            Notification.Builder builder = new Notification.Builder(context);
            Bitmap bitmap = BmpUtil.drawable2Bitmap(AppUtil.getAppIcon(context));
            builder.setContentTitle("title")
                    .setContentText("content")
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000})
                    // 声音和震动
//                    .setDefaults(Notification.DEFAULT_SOUND & Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setSound(Uri.fromFile(new File(xxxx)))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setSmallIcon(Icon.createWithBitmap(bitmap))
                        .setLargeIcon(Icon.createWithResource(context, R.drawable.baseline_message_24));
            } else {
                builder.setSmallIcon(R.drawable.baseline_message_24)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.baseline_message_24));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(channelId);
            }

            // 设置通知主题的意图
//            Intent resultIntent = new Intent(this, TaskActivity.class);
//            PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                    contex, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            Intent intentCancel = new Intent(TestBroadcastReceiver.ACTION);
//            Bean bean = new Bean();
//            bean.setId(new Random().nextInt(1000) + 1L);
//            ZLog.v(ZTag.TAG_DEBUG, "zaze send bean : " + bean.getId());
//            intentCancel.putExtra(TestBroadcastReceiver.TEST, bean);
//            PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 1, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.setContentIntent(pendingIntentCancel);
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationManager mNotificationManager = HookNotificationManager.hookNotificationManager(context);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话类型", NotificationManager.IMPORTANCE_DEFAULT);
//                mNotificationManager.createNotificationChannel(channel);
//            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ZLog.w(ZTag.TAG_ERROR, "need permission: Manifest.permission.POST_NOTIFICATIONS");
                return;
            }
            NotificationManagerCompat.from(context).notify(11, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}