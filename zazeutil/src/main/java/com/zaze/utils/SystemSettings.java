package com.zaze.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Deprecated
    public static long getBootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000;
    }

    /**
     * 设置息屏时间
     *
     * @param context    context
     * @param timeMillis timeMillis
     */
    public static void setScreenCloseTime(Context context, int timeMillis) {
        ZLog.i(ZTag.TAG_SYSTEM, "设置屏幕关闭时间 : %s秒", timeMillis);
        Settings.System.putInt(context.getContentResolver(), SCREEN_OFF_TIMEOUT, timeMillis);
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
}
