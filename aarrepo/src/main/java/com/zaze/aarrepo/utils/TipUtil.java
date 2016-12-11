package com.zaze.aarrepo.utils;

import android.content.Context;
import android.widget.Toast;

import com.zaze.aarrepo.commons.base.BaseApplication;

public class TipUtil {

    public static void toast(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int duration) {
        toast(BaseApplication.getInstance(), msg, duration);
    }

    public static void toast(final Context context, String msg, int duration) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
