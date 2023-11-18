package com.zaze.demo.debug.wifi;

import android.Manifest;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.zaze.demo.app.MyApplication;
import com.zaze.demo.receiver.WifiReceiver;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-27 - 20:25
 */
public class WifiCompat {

    public static final int WIFI_JOB_ID = 11;
    private static final long WIFI_JOB_PERIODIC = 5 * 1000L;

    /**
     * Android 7以上使用JobSchedule 监听wifi变化
     * 以下使用动态注册广播监听
     *
     * @param context Application
     */
    public static void listenerByJob(Application context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            ComponentName componentName = new ComponentName(context, WifiJob.class);
//            JobInfo jobinfo = new JobInfo.Builder(WIFI_JOB_ID, componentName)
//                    .setPeriodic(WIFI_JOB_PERIODIC)
//                    .build();

            JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(
                    WIFI_JOB_ID, new ComponentName(context, WifiJob.class));
//            builder.addTriggerContentUri(new JobInfo.TriggerContentUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS));
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//            builder.setRequiresDeviceIdle(true);
//            builder.setRequiresCharging(true);
            if (js != null) {
                js.cancel(WIFI_JOB_ID);
                js.schedule(builder.build());
            }
        } else {
            listenerByBroadcast(context);
        }
    }


    /**
     * Android 5以上使用connectivityManager.requestNetwork 设置监听
     * 以下使用动态注册广播监听
     */
    public static void listenerByConn(ConnectivityManager.NetworkCallback networkCallback) {
        Application context = MyApplication.getInstance();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 保证获取权限
            if (networkCallback != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), networkCallback);
            }
        } else {
            ZLog.e(ZTag.TAG_DEBUG, "checkSelfPermission denied!!");
        }
    }

    /**
     * 使用动态注册广播监听
     * 5.0前
     * @param context Application
     */
    public static void listenerByBroadcast(Application context) {
        context.registerReceiver(new WifiReceiver(), WifiReceiver.Companion.createIntentFilter());
    }
}
