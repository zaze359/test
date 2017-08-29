package com.zaze.utils;

import android.content.Context;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class ZTipUtil {

    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String msg, int duration) {
        makeText(context, msg, duration).show();
    }
}
