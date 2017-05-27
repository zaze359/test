package com.zaze.aarrepo.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Process;

import com.zaze.aarrepo.commons.log.ZLog;

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

    public static PackageInfo getPackageArchiveInfo(Context context, String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return null;
        }
        return context.getPackageManager().getPackageArchiveInfo(fileName, 0);
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static List<ResolveInfo> getInstalledApp(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public static List<PackageInfo> getInstalledPackage(Context context, int flag) {
        return context.getPackageManager().getInstalledPackages(flag);
    }

    public static List<ApplicationInfo> getInstalledApplications(Context context, int flag) {
        return context.getPackageManager().getInstalledApplications(0);
    }

    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            ZLog.e("", "没有找到应用 %s", packageName);
            return null;
        }
    }

    // --------------------------------------------------

    /**
     * 安装应用
     *
     * @param context  context
     * @param filePath 文件绝对路径
     */
    public static int installApk(Context context, String filePath) {
        ZLog.i(ZTag.TAG_ABOUT_APP, "start installApk %s", filePath);
        if (DeviceUtil.checksRoot()) {
            if (installApkSilent(filePath)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "start 正常安装");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return 0;
        }
    }

    /**
     * 卸载应用
     *
     * @param context     context
     * @param packageName packageName
     */
    public static int unInstallApk(Context context, String packageName) {
        if (DeviceUtil.checksRoot()) {
            if (unInstallApkSilent(packageName)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "正常卸载");
            Intent uninstallIntent = new Intent();
            uninstallIntent.setAction(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + packageName));
            context.startActivity(uninstallIntent);
            return 0;
        }
    }

    /**
     * 静默安装
     *
     * @param filePath 文件绝对路径
     */
    public static boolean installApkSilent(String filePath) {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默安装 %s", filePath);
        if (RootCmd.execRootCmd("pm install -r " + filePath) != RootCmd.ERROR) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装成功!");
            return true;
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装失败!");
            return false;
        }
    }

    /**
     * 静默卸载
     *
     * @param packageName 报名
     */
    public static boolean unInstallApkSilent(String packageName) {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默卸载 %s", packageName);
        if (RootCmd.execRootCmd("pm uninstall " + packageName) != RootCmd.ERROR) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载成功!");
            return true;
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载失败!");
            return false;
        }
    }

    // --------------------------------------------------

    /**
     * 清理data数据
     *
     * @param packageName packageName
     */
    public static void clearDataInfo(String packageName) {
        File file = new File("/data/data/" + packageName);
        if (file.exists()) {
            RootCmd.execRootCmd("pm clear " + packageName);
        }
    }
    // --------------------------------------------------

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
     * 删除所有运行的进程
     *
     * @param context context
     */
    public static void killAllRunningProcess(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
                if (!processInfo.processName.equals(context.getPackageName())
                        && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Process.killProcess(processInfo.pid);
                    activityManager.killBackgroundProcesses(processInfo.processName);
                }
            }
        }
    }
    // --------------------------------------------------


    /**
     * 获取app名
     *
     * @param context
     * @param packageName
     * @return app名
     */
    public static String getAppName(Context context, String packageName, String defaultName) {
        ApplicationInfo applicationInfo = getApplicationInfo(context, packageName);
        if (applicationInfo == null) {
            return defaultName;
        } else {
            return context.getPackageManager().getApplicationLabel(applicationInfo).toString();
        }
    }

    // --------------------------------------------------

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            ZLog.e(ZTag.TAG_ERROR, e.getMessage());
            return null;
        }
    }

    /**
     * @param context context
     * @return 应用版本名
     */
    public static String getAppVersion(Context context) {
        return getAppVersion(context, context.getPackageName());
    }

    /**
     * @param context context
     * @return 应用版本名
     */
    public static String getAppVersion(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * @param context context
     * @return 应用版本号
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * @param context context
     * @return 应用版本号
     */
    public static int getAppVersionCode(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
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
