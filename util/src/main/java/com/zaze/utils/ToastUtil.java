package com.zaze.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String msg, int duration) {
        if (context != null) {
            Toast.makeText(context, msg, duration).show();
        }
    }
}
