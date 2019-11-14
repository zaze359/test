package com.zaze.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description : from in.srain.cube.util
 * date : 2016-03-28 - 15:44
 */
public class DisplayUtil {

    private static int screenWidthPixels;
    private static int screenHeightPixels;
    // --------------------------------------------------
    private static float screenDensity;
    private static int screenDensityDpi;
    // --------------------------------------------------
    private static float screenWidthDp;
    private static float screenHeightDp;
    // --------------------------------------------------

    private static DisplayMetrics metrics;
    private static final String TIP_TO_INIT = "请调用init()方法初始化后, 使用该方法！";

    public static void init(Context context) {
        metrics = context.getResources().getDisplayMetrics();
        screenWidthPixels = metrics.widthPixels;
        screenHeightPixels = metrics.heightPixels;
        screenDensity = metrics.density;
        screenDensityDpi = metrics.densityDpi;
        screenWidthDp = dpiFromPx(screenWidthPixels);
        screenHeightDp = dpiFromPx(screenHeightPixels);
    }

    public static DisplayMetrics getMetrics() {
        return metrics;
    }

    public static int getScreenWidthPixels() {
        return screenWidthPixels;
    }

    public static int getScreenHeightPixels() {
        return screenHeightPixels;
    }

    public static float getScreenDensity() {
        return screenDensity;
    }

    public static int getScreenDensityDpi() {
        return screenDensityDpi;
    }

    public static float getScreenWidthDp() {
        return screenWidthDp;
    }

    public static float getScreenHeightDp() {
        return screenHeightDp;
    }

    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * @param dp dp
     * @return px
     * @deprecated {@link DisplayUtil#pxFromDp(float dp)}
     */
    @Deprecated
    public static float dp2px(float dp) {
        return dp * screenDensity;
    }

    // --------------------------------------------------

    /**
     * px 转 dpi
     *
     * @param px
     * @return
     */
    public static float dpiFromPx(int px) {
        if (metrics != null) {
            float densityRatio = metrics.densityDpi * 1.0f / DisplayMetrics.DENSITY_DEFAULT;
            return (px / densityRatio);
        } else {
            ZLog.e(ZTag.TAG_ERROR, TIP_TO_INIT);
            return 0;
        }
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static float pxFromDp(float dp) {
        if (metrics != null) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        } else {
            ZLog.e(ZTag.TAG_ERROR, TIP_TO_INIT);
            return 0;
        }
    }

    /**
     * sp 转 px
     *
     * @param sp sp
     * @return
     */
    public static float pxFromSp(float sp) {
        if (metrics != null) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
        } else {
            ZLog.e(ZTag.TAG_ERROR, TIP_TO_INIT);
            return 0;
        }
    }

    // --------------------------------------------------


    public static String getDensityDpiName() {
        if (screenDensityDpi <= DisplayMetrics.DENSITY_LOW) {
            return "ldpi";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            return "mdpi";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_TV) {
            return "tv";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return "hdpi";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return "xhdpi";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_XXHIGH) {
            return "xxhdpi";
        } else if (screenDensityDpi <= DisplayMetrics.DENSITY_XXXHIGH) {
            return "xxxhdpi";
        } else {
            return "x????dpi";
        }
    }

//    public static int designedDP2px(float designedDp) {
//        if (screenWidthDp != 320) {
//            designedDp = designedDp * screenWidthDp / 320f;
//        }
//        return dp2px(designedDp);
//    }
//
//    public static void setPadding(final View view, float left, float top, float right, float bottom) {
//        view.setPadding(designedDP2px(left), dp2px(top), designedDP2px(right), dp2px(bottom));
//    }
}
