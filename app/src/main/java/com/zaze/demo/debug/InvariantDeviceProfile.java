package com.zaze.demo.debug;

import android.util.DisplayMetrics;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-06-29 - 10:38
 */
public class InvariantDeviceProfile {

    private static final float ICON_SIZE_DEFINED_IN_APP_DP = 48;

    private int iconBitmapSize;

    private int fillResIconDpi;

    public int getIconBitmapSize() {
        return iconBitmapSize;
    }

    public int getFillResIconDpi() {
        return fillResIconDpi;
    }


    /**
     * 获取应用icon的像素密度
     *
     * @param requiredSize requiredSize
     * @return 像素密度
     */
    public int getLauncherIconDensity(int requiredSize) {
        int[] densityBuckets = new int[]{
                DisplayMetrics.DENSITY_LOW,
                DisplayMetrics.DENSITY_MEDIUM,
                DisplayMetrics.DENSITY_TV,
                DisplayMetrics.DENSITY_HIGH,
                DisplayMetrics.DENSITY_XHIGH,
                DisplayMetrics.DENSITY_XXHIGH,
                DisplayMetrics.DENSITY_XXXHIGH
        };
        int density = DisplayMetrics.DENSITY_XXXHIGH;
        for (int i = densityBuckets.length - 1; i >= 0; i--) {
            float expectedSize = ICON_SIZE_DEFINED_IN_APP_DP * densityBuckets[i]
                    / DisplayMetrics.DENSITY_DEFAULT;
            if (expectedSize >= requiredSize) {
                density = densityBuckets[i];
            }
        }

        return density;
    }
}
