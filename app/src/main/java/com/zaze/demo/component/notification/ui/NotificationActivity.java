package com.zaze.demo.component.notification.ui;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.notification.presenter.NotificationPresenter;
import com.zaze.demo.component.notification.presenter.impl.NotificationPresenterImpl;
import com.zaze.demo.component.notification.service.NotificationService;
import com.zaze.demo.component.notification.view.NotificationView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-08 03:56 1.0
 */
public class NotificationActivity extends ZBaseActivity implements NotificationView {
    private NotificationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
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
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("title");
        builder.setContentText("content");
        builder.setAutoCancel(true);
        // 设置通知主题的意图
//        Intent resultIntent = new Intent(this, TaskActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());
    }

}