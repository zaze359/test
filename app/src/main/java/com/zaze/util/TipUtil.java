package com.zaze.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Description :
 * @author : ZAZE
 * @version : 2016-05-11 - 09:38
 */
public class TipUtil {
    /**
     * 提示
     * @param str
     */
    public static void toastDisplay(String str, Context context) {
        if (str == null)
            return;
        if ("".equals(str))
            return;

        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
