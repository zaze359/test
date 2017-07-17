package com.zaze.aarrepo.utils;

import android.content.Context;
import android.widget.Toast;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.log.ZLog;

import static android.widget.Toast.makeText;

public class TipUtil {

    public static void toast(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int duration) {
        try {
            toast(ZBaseApplication.getInstance(), msg, duration);
        } catch (Exception e) {
            e.printStackTrace();
            ZLog.e(ZTag.TAG_DEBUG, "if you want use this method, please use ZBaseApplication");
        }
    }

    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String msg, int duration) {
        makeText(context, msg, duration).show();
    }
}
