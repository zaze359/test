package com.zaze.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Description :
 * date : 2015-12-10 - 11:23
 *
 * @author : zaze
 * @version : 1.0
 */
public class AppUtil {
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static List<ResolveInfo> getInstalledApp(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public static List<PackageInfo> getInstalledPackage(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager pManager = context.getPackageManager();
            PackageInfo packageInfo = pManager.getPackageInfo(packageName, 0);
            return pManager.getApplicationIcon(packageInfo.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
