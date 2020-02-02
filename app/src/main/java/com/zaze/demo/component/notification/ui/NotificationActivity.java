package com.zaze.demo.component.notification.ui;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.notification.presenter.NotificationPresenter;
import com.zaze.demo.component.notification.presenter.impl.NotificationPresenterImpl;
import com.zaze.demo.component.notification.service.NotificationService;
import com.zaze.demo.component.notification.view.NotificationView;
import com.zaze.demo.hook.HookNotificationManager;


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

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(17301651)
                    .setContentTitle("title")
                    .setContentText("content")
                    .setAutoCancel(true)
                    .setChannelId(context.getPackageName());
//                .setOngoing(true)
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
            NotificationManager mNotificationManager = HookNotificationManager.hookNotificationManager(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        context.getPackageName(),
                        "会话类型",//这块Android9.0分类的比较完整，你创建多个这样的东西，你可以在设置里边显示那个或者第几个
                        NotificationManager.IMPORTANCE_DEFAULT

                );
                mNotificationManager.createNotificationChannel(channel);
            }
            mNotificationManager.notify(123, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}