package com.zaze.demo.component.notification.ui;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.notification.presenter.NotificationPresenter;
import com.zaze.demo.component.notification.presenter.impl.NotificationPresenterImpl;
import com.zaze.demo.component.notification.receiver.Bean;
import com.zaze.demo.component.notification.receiver.TestBroadcastReceiver;
import com.zaze.demo.component.notification.service.NotificationService;
import com.zaze.demo.component.notification.view.NotificationView;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.Random;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-08 03:56 1.0
 */
public class NotificationActivity extends BaseActivity implements NotificationView {
    private NotificationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        presenter = new NotificationPresenterImpl(this);
        findViewById(R.id.notification_1_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(NotificationActivity.this, NotificationService.class));
            }
        });
        findViewById(R.id.notification_2_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification();
            }
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

    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("content")
                .setAutoCancel(true)
                .setChannelId(this.getPackageName());
//                .setOngoing(true)
        // 设置通知主题的意图
//        Intent resultIntent = new Intent(this, TaskActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intentCancel = new Intent(TestBroadcastReceiver.ACTION);
        Bean bean = new Bean();
        bean.setId(new Random().nextInt(1000) + 1L);
        ZLog.v(ZTag.TAG_DEBUG, "zaze send bean : " + bean.getId());
        intentCancel.putExtra(TestBroadcastReceiver.TEST, bean);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 1, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntentCancel);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    this.getPackageName(),
                    "会话类型",//这块Android9.0分类的比较完整，你创建多个这样的东西，你可以在设置里边显示那个或者第几个
                    NotificationManager.IMPORTANCE_DEFAULT

            );
            mNotificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(1, builder.build());
    }

}