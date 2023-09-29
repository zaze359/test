package com.zaze.demo.feature.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.graphics.drawable.toDrawable
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.initToolbar
import com.zaze.demo.feature.notification.databinding.ActivityNotificationBinding
import com.zaze.utils.AppUtil
import com.zaze.utils.BmpUtil
import com.zaze.utils.IdGenerator
import com.zaze.utils.ext.getDrawable
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-08 03:56 1.0
 */
class NotificationActivity : AbsActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun getPermissionsToRequest(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            super.getPermissionsToRequest()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.appbarLayout.toolbar) {
            title = getString(R.string.notification_title)
        }

        val context: ContextWrapper = object : ContextWrapper(this) { //            @Override
            //            public String getPackageName() {
            //                return "android";
            //            }
        }
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
        } catch (e: Throwable) {

        }
        binding.normalNotificationBtn.setOnClickListener { _ ->
            configNotification(
                context,
                "channelId_1",
                111
            )
        }
        binding.serviceNotificationBtn.setOnClickListener { _ ->
            startService(
                Intent(context, NotificationService::class.java)
            )
        }
        binding.clearAllBtn.setOnClickListener {
            val mNotificationManager = this@NotificationActivity.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            mNotificationManager.cancelAll()
            stopService(Intent(this@NotificationActivity, NotificationService::class.java))
        }
    }

    private fun configNotification(context: Context, channelId: String, notifyId: Int) {
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setContentTitle("title")
            .setContentText("content")
//            .setDefaults(Notification.DEFAULT_ALL)
            .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            .setLargeIcon(BmpUtil.drawable2Bitmap(AppUtil.getAppIcon(context)))
            .setAutoCancel(true)

        // 配置震动模式，数组依次表示 静止时长，震动时长.....。这样循环
        val vibrate = longArrayOf(1000, 3000, 1000, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 若之前Defaults中 没有设置声音和震动，这里即使设置了可能也无法正常震动和响铃，需要修改一下 channelId 处理。
            // NotificationManager.IMPORTANCE_DEFAULT 根据系统提示声音、震动等，状态栏显示，但是不会干扰用户
            // NotificationManager.IMPORTANCE_HIGH 会弹出通知，干扰到用户，并展示所有到配置
            // NotificationManager.IMPORTANCE_MIN 静默，没有声音、震动、状态栏不显示
            // NotificationManager.IMPORTANCE_LOW 状态栏不显示中显示，但是没有声音和震动
            val notificationChannel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.vibrationPattern = vibrate
            // LED灯
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
//            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            // 创建 channel
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        } else {
            builder.setVibrate(vibrate) // 声音和震动
                //                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                //                    .setSound(Uri.fromFile(new File(xxxx)))
                .setLights(Color.RED, 2000, 2000).priority =
                Notification.PRIORITY_HIGH
        }
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bitmap = BmpUtil.drawable2Bitmap(AppUtil.getAppIcon(context))
            builder
                .setSmallIcon(R.drawable.ic_message)
//                .setSmallIcon(IconCompat.createWithBitmap(bitmap!!))
//                .setLargeIcon(R.drawable.ic_message.getDrawable(context)?.toBitmapOrNull())
                .setLargeIcon(bitmap)
        } else {
            builder.setSmallIcon(R.drawable.ic_message)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_message
                    )
                )
        }
        //
        // 添加pendingIntent, 响应点击通知栏事件
        val contentIntent: PendingIntent
        var flag = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = flag or PendingIntent.FLAG_IMMUTABLE
        }
        contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent("com.zaze.demo.action.ENTRANCE"), flag
        )
        builder.setContentIntent(contentIntent)
        // 显示通知
        // style
        builder.setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(
                BitmapFactory.decodeResource(
                    resources, R.drawable.notification_big_img
                )
            )
        )
        builder.setStyle(
            NotificationCompat.BigTextStyle().bigText(
                "asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfa" +
                        "sdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasd" +
                        "fasdfasdfasdfasdfasdfsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf"
            )
        )
        notify(context, builder, notifyId)
    }

    private fun notify(context: Context, builder: NotificationCompat.Builder, notifyId: Int) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ZLog.w(ZTag.TAG_ERROR, "need permission: Manifest.permission.POST_NOTIFICATIONS")
            return
        }
        NotificationManagerCompat.from(context).notify(notifyId, builder.build())
    }
}