package com.zaze.aarrepo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Process;

import com.zaze.aarrepo.commons.log.LogKit;

import java.io.File;
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


    /**
     * 安装应用
     *
     * @param context  context
     * @param filePath 文件绝对路径
     */
    public static void installApk(Context context, String filePath) {
        LogKit.i("start installApk %s", filePath);
        if (DeviceUtil.checksRoot()) {
            installApkSilent(filePath);
        } else {
            LogKit.i("start 正常安装");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 卸载应用
     *
     * @param context     context
     * @param packageName packageName
     */
    public static void unInstallApk(Context context, String packageName) {
        if (DeviceUtil.checksRoot()) {
            unInstallApkSilent(packageName);
        } else {
            LogKit.i("正常卸载");
            Intent uninstallIntent = new Intent();
            uninstallIntent.setAction(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + packageName));
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 静默安装
     *
     * @param filePath 文件绝对路径
     */
    public static void installApkSilent(String filePath) {
        LogKit.i("start 静默安装 %s", filePath);
        String cmd = "pm install -r " + filePath;
        RootCmd.execRootCmdSilent(cmd);
        LogKit.i("end 静默安装");
    }

    /**
     * 静默卸载
     *
     * @param packageName 报名
     */
    public static void unInstallApkSilent(String packageName) {
        LogKit.i("start 静默卸载 %s", packageName);
        String cmd = "pm uninstall " + packageName;
        RootCmd.execRootCmdSilent(cmd);
        LogKit.i("end 静默卸载");
    }

    /**
     * 清理data数据
     *
     * @param packageName packageName
     */
    public static void clearDataInfo(String packageName) {
        File file = new File("/data/data/" + packageName);
        if (file.exists()) {
            String clear = "pm clear " + packageName;
            RootCmd.execRootCmd(clear);
        }
    }

    /**
     * 杀进程
     *
     * @param context context
     */
    public static void killProcess(Context context) {
        Process.killProcess(Process.myPid());
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(getAppPackageName(context));
    }

    /**
     * @param context context
     * @return 应用版本名
     */
    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param context context
     * @return 应用版本号
     */
    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param context
     * @param packageName
     * @return 应用图标
     */
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
