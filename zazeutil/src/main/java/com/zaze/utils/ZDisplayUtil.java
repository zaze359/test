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
public class ZDisplayUtil {

    private static int screenWidthPixels;
    private static int screenHeightPixels;
    // --------------------------------------------------
    private static float screenDensity;
    private static int screenDensityDpi;
    // --------------------------------------------------
    private static float screenWidthDp;
    private static float screenHeightDp;
    // --------------------------------------------------
    private static boolean isInitialed;

    private static DisplayMetrics metrics;
    private static final String TIP_TO_INIT = "请在application中调用init()方法初始化后,使用该方法！";

    public static void init(Context context) {
        if (isInitialed || context == null) {
            return;
        }
        isInitialed = true;
        metrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getMetrics(metrics);
        metrics = context.getResources().getDisplayMetrics();
        screenWidthPixels = metrics.widthPixels;
        screenHeightPixels = metrics.heightPixels;
        screenDensity = metrics.density;
        screenDensityDpi = metrics.densityDpi;
        screenWidthDp = dpiFromPx(screenWidthPixels);
        screenHeightDp = dpiFromPx(screenHeightPixels);
    }


    /**
     * 是否已经初始化
     *
     * @return boolean
     */
    public static boolean isIsInitialed() {
        return isInitialed;
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
     * @deprecated {@link ZDisplayUtil#pxFromDp(float dp)}
     */
    @Deprecated
    public static int dp2px(float dp) {
        final float scale = screenDensity;
        return (int) (dp * scale + 0.5f);
    }

    // --------------------------------------------------

    /**
     * px 转 dpi
     *
     * @param px
     * @return
     */
    public static float dpiFromPx(int px) {
        if (isInitialed) {
            float densityRatio = (float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
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
    public static int pxFromDp(float dp) {
        if (isInitialed) {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics));
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
    public static int pxFromSp(float sp) {
        if (isInitialed) {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics));
        } else {
            ZLog.e(ZTag.TAG_ERROR, TIP_TO_INIT);
            return 0;
        }
    }

    // --------------------------------------------------


    public static String getDensityDpiName() {
        if (screenDensityDpi <= LDPI) {
            return "ldpi";
        } else if (screenDensityDpi <= MDPI) {
            return "mdpi";
        } else if (screenDensityDpi <= HDPI) {
            return "hdpi";
        } else if (screenDensityDpi <= XHDPI) {
            return "xhdpi";
        } else if (screenDensityDpi <= XXHDPI) {
            return "xxhdpi";
        } else if (screenDensityDpi <= XXXHDPI) {
            return "xxxhdpi";
        } else {
            return "x????dpi";
        }
    }

    public static final int LDPI = 120;
    public static final int MDPI = 160;
    public static final int HDPI = 240;
    public static final int XHDPI = 320;
    public static final int XXHDPI = 480;
    public static final int XXXHDPI = 640;


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
