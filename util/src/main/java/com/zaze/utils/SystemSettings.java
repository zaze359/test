package com.zaze.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-10-18 - 16:47
 */
public class SystemSettings {

    /**
     * 获取开机时间
     *
     * @return 开机时间
     */
    public static long getBootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    /**
     * 设置息屏时间
     *
     * @param context    context
     * @param timeMillis timeMillis
     */
    public static void setScreenCloseTime(Context context, int timeMillis) {
        ZLog.i(ZTag.TAG_SYSTEM, ZStringUtil.format("设置屏幕关闭时间 : %s秒", timeMillis));
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeMillis);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置屏幕亮度
     *
     * @param context context
     * @param value   value
     */
    public static void setScreenBrightness(Context context, int value) {
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, value);
        contentResolver.notifyChange(uri, null);
    }

    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true;
        }
    }
}
