package com.zaze.aarrepo.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Process
import com.zaze.aarrepo.commons.log.ZLog
import java.io.File

/**
 * Description : App Util
 * @author : ZAZE
 * @version : 2017-05-27 - 17:23
 */
open class ZAppUtil {
    companion object {
//    /**
//     * Description :
//     *
//     * @author zaze
//     * @version 2017/5/31 - 下午3:40 1.0
//     */
//    fun getAppPackageName(context: Context): String {
//        return context.packageName
//    }
        /**
         * [context] context
         * [packageName] null 使用context 的包名
         * @return 应用版本名
         */
        fun getAppVersionName(context: Context, packageName: String?): String {
            val packageInfo = getPackageInfo(context, packageName ?: context.packageName)
            if (packageInfo != null) {
                return packageInfo.versionName
            } else {
                return ""
            }
        }

        /**
         * [context]
         * [packageName] null 使用context 的包名
         * @return 应用版本号
         */
        fun getAppVersionCode(context: Context, packageName: String?): Int {
            val packageInfo = getPackageInfo(context, packageName ?: context.packageName)
            if (packageInfo != null) {
                return packageInfo.versionCode
            } else {
                return 0
            }
        }

        /**
         * 获取app名
         * [context]
         * [packageName]
         * [defaultName]
         * @return app名
         */
        fun getAppName(context: Context, packageName: String, defaultName: String): String {
            val applicationInfo = getApplicationInfo(context, packageName)
            if (applicationInfo == null) {
                return defaultName
            } else {
                return context.packageManager.getApplicationLabel(applicationInfo).toString()
            }
        }
        // --------------------------------------------------

        /**
         * Description :
         *
         * @author zaze
         * @version 2017/5/31 - 下午3:46 1.0
         */
        fun getPackageInfo(context: Context, packageName: String): PackageInfo? {
            val packageManager = context.packageManager
            try {
                return packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                ZLog.e(ZTag.TAG_ERROR, e.message)
                return null
            }
        }

        /**
         * Description :
         *
         * [context] context
         * [packageName] packageName
         * @author zaze
         * @version 2017/5/31 - 下午3:40 1.0
         */
        fun getApplicationInfo(context: Context, packageName: String): ApplicationInfo? {
            try {
                return context.packageManager.getApplicationInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                ZLog.e(ZTag.TAG_ABOUT_APP, "没有找到应用信息 : $packageName")
                return null
            }
        }

        // --------------------------------------------------
        /**
         * Description : 应用是否安装
         * [context]
         * [packageName]
         * @author zaze
         * @version 2017/5/31 - 下午3:47 1.0
         */
        fun isAppInstalled(context: Context, packageName: String): Boolean {
            return getApplicationInfo(context, packageName) != null
        }

        /**
         * Description : 安装应用
         *
         * [context] 上下文
         * [filePath] 文件绝对路径
         *
         * @author zaze
         * @version 2017/5/31 - 下午2:54 1.0
         */
        fun installApp(context: Context, filePath: String) {
            val file = File(filePath)
            if (file.exists()) {
                ZLog.i(ZTag.TAG_ABOUT_APP, "准备安装 $filePath")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
                context.startActivity(intent)
            } else {
                ZLog.e(ZTag.TAG_ABOUT_APP, "$filePath isn't exists")
            }
        }

        /**
         * Description : 卸载应用
         * [context] 上下文
         * [packageName] 需要卸载应用的包名
         * @author zaze
         * @version 2017/5/31 - 下午2:57 1.0
         */
        fun unInstallApp(context: Context, packageName: String) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "开始卸载 $packageName")
            val uninstallIntent = Intent()
            uninstallIntent.action = Intent.ACTION_DELETE
            uninstallIntent.data = Uri.parse("package:$packageName")
            context.startActivity(uninstallIntent)
        }
        // --------------------------------------------------

        /**
         * Description : 返回 应用所在进程
         * [context] context
         * [packageName] packageName
         * @author zaze
         * @version 2017/5/31 - 下午2:59 1.0
         */
        fun getAppProcess(context: Context, packageName: String): ActivityManager.RunningAppProcessInfo? {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return activityManager.runningAppProcesses.firstOrNull { it.pkgList.contains(packageName) }
        }

        /**
         * Description : app 是否在运行中
         * [context] 上下文
         * [packageName] 包名
         * @author zaze
         * @version 2017/5/22 - 下午3:32 1.0
         */
        fun isAppRunning(context: Context, packageName: String): Boolean {
            return getAppProcess(context, packageName) != null
        }

        /**
         * Description : 杀指定包名所在的进程
         * [context] 上下文
         * [packageName] 包名
         * @author zaze
         * @version 2017/5/22 - 下午3:32 1.0
         */
        fun killAppProcess(context: Context, packageName: String) {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.killBackgroundProcesses(packageName)
            val processInfo = getAppProcess(context, packageName);
            if (processInfo != null) {
                Process.killProcess(processInfo.pid)
            }
        }

        // --------------------------------------------------
        /**
         * 清理data数据

         * @param packageName packageName
         */
        fun clearDataInfo(context: Context, packageName: String) {
            if (RootCmd.isRoot()) {
                RootCmd.execRootCmd("pm clear " + packageName)
            } else {
                FileUtil.deleteFile("/data/data/" + packageName)
                killAppProcess(context, packageName)
            }
//            FileUtil.deleteFile("/data/data/" + packageName)
//            killAppProcess(context, packageName)
        }
    }

}