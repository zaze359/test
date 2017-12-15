package com.zaze.utils

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.os.Process
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File


/**
 * Description : App Util
 * @author : ZAZE
 * @version : 2017-05-27 - 17:23
 */
object ZAppUtil {

    /**
     * [context] context
     * [packageName] null 使用context 的包名
     * @return 应用版本名
     */
    fun getAppVersionName(context: Context, packageName: String? = null): String {
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
    fun getAppVersionCode(context: Context, packageName: String? = null): Int {
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
    fun getAppName(context: Context, packageName: String? = null, defaultName: String = "未知"): String {
        val applicationInfo = getApplicationInfo(context, packageName)
        if (applicationInfo == null) {
            return defaultName
        } else {
            return context.packageManager.getApplicationLabel(applicationInfo).toString()
        }
    }

    /**
     * [context]
     * [packageName]
     * @return 应用图标
     */
    fun getAppIcon(context: Context, packageName: String? = null): Drawable? {
        try {
            val pManager = context.packageManager
            val packageInfo = pManager.getPackageInfo(packageName ?: context.packageName, 0)
            return pManager.getApplicationIcon(packageInfo.applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_DEBUG, e.message)
            return null
        }

    }

    /**
     * [context] context
     * [packageName] packageName
     * @author zaze
     * @version 2017/5/31 - 下午3:40 1.0
     */
    fun getApplicationInfo(context: Context, packageName: String? = null): ApplicationInfo? {
        try {
            return context.packageManager.getApplicationInfo(packageName ?: context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_ABOUT_APP, "没有找到应用信息 : $packageName")
            return null
        }
    }
    // --------------------------------------------------

    /**
     * Description : 获取对应包名的PackageInfo
     * [context] context
     * [packageName] 包名
     * @author zaze
     * @version 2017/5/31 - 下午3:46 1.0
     */
    fun getPackageInfo(context: Context, packageName: String? = null): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(packageName ?: context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_DEBUG, "PackageManager.NameNotFoundException : $packageName")
            return null
        }
    }

    /**
     * Description : 获取apk文件的PackageInfo
     * [context] context
     * [fileName] fileName
     * @author zaze
     * @version 2017/8/26 - 下午3:23 1.0
     */
    fun getPackageArchiveInfo(context: Context, fileName: String): PackageInfo? {
        if (TextUtils.isEmpty(fileName)) {
            return null
        }
        return context.packageManager.getPackageArchiveInfo(fileName, 0)
    }

    // --------------------------------------------------
    fun queryIntentActivities(context: Context, packageName: String): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        mainIntent.`package` = packageName
        return context.packageManager.queryIntentActivities(mainIntent, 0)
    }

    fun getInstalledPackages(context: Context, flag: Int = 0): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(flag)
    }

    fun getInstalledApplications(context: Context, flag: Int = 0): List<ApplicationInfo> {
        return context.packageManager.getInstalledApplications(flag)
    }
    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : app 是否在运行中
     * [context] 上下文
     * [packageName] 包名
     * @author zaze
     * @version 2017/5/22 - 下午3:32 1.0
     */
    fun isAppRunning(context: Context, packageName: String): Boolean {
        if (getAppProcess(context, packageName).isEmpty()) {
            return getAppPid(packageName) > 0
        } else {
            return true
        }
    }

    /**
     * 是否是系统应用
     * [context] context
     * [intent] intent
     */
    fun isSystemApp(context: Context, intent: Intent): Boolean {
        val componentName = intent.component
        var packageName: String? = null
        if (componentName == null) {
            val info = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (info?.activityInfo != null) {
                packageName = info.activityInfo.packageName
            }
        } else {
            packageName = componentName.packageName
        }
        return isSystemApp(context, packageName)
    }

    /**
     * 是否是系统应用
     * [context] context
     * [packageName] packageName
     */
    fun isSystemApp(context: Context, packageName: String?): Boolean {
        return if (TextUtils.isEmpty(packageName)) {
            false
        } else {
            val packageInfo = getPackageInfo(context, packageName)
            (packageInfo?.applicationInfo != null
                    && packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0)
        }
    }


    /**
     * Description : 应用是否安装
     * [context]
     * [packageName]
     * @author zaze
     * @version 2017/5/31 - 下午3:47 1.0
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        return getApplicationInfo(context, packageName) != null
    }

    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : 安装应用
     *
     * [context] 上下文
     * [filePath] 文件绝对路径
     *
     * @author zaze
     * @version 2017/5/31 - 下午2:54 1.0
     */
    fun install(context: Context, filePath: String) {
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
    fun unInstall(context: Context, packageName: String? = null) {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始卸载 ${packageName ?: context.packageName}")
        val uninstallIntent = Intent()
        uninstallIntent.action = Intent.ACTION_DELETE
        uninstallIntent.data = Uri.parse("package:${packageName ?: context.packageName}")
        context.startActivity(uninstallIntent)
    }


    /**
     * 静默安装
     * [filePath] 文件绝对路径
     */
    fun installApkSilent(filePath: String): Boolean {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默安装 %s", filePath)
        if (ZCommand.isSuccess(ZCommand.execRootCmdForRes("pm install -r " + filePath))) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装成功!")
            return true
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装失败!")
            return false
        }
    }

    /**
     * 静默卸载
     * [packageName] 报名
     */
    fun unInstallApkSilent(packageName: String): Boolean {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默卸载 %s", packageName)
        if (ZCommand.isSuccess(ZCommand.execRootCmdForRes("pm uninstall " + packageName))) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载成功!")
            return true
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载失败!")
            return false
        }
    }

    // --------------------------------------------------

    fun getAppPid(packageName: String): Int {
        ZLog.i(ZTag.TAG_DEBUG, "getAppPid : " + packageName)
        if (ZCommand.isCommandExists("grep")) {
            val result = ZCommand.execCmdForRes("ps | grep " + packageName)
            if (ZCommand.isSuccess(result) && result.successList.size > 0) {
                val message = result.successList[0]
                ZLog.i(ZTag.TAG_DEBUG, "getAppPid : " + message)
                val fields = message.split("\\s+".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                if (fields.size > 1) {
                    return ZStringUtil.parseInt(fields[1])
                }
            }
        }
        return 0
    }

    /**
     * Description : 返回 应用所在进程
     * [context] context
     * [packageName] packageName
     * @author zaze
     * @version 2017/5/31 - 下午2:59 1.0
     */
    fun getAppProcess(context: Context, packageName: String): ArrayList<ActivityManager.RunningAppProcessInfo> {
        val activityManager = getActivityManager(context)
        val list = ArrayList<ActivityManager.RunningAppProcessInfo>()
        activityManager.runningAppProcesses.filterTo(list) { it.pkgList.contains(packageName) }
        return list
    }

    /**
     * Description : MemoryInfo
     * @author zaze
     * @version 2017/10/9 - 下午1:51 1.0
     */
    fun getProcessMemoryInfo(context: Context, pids: IntArray): Array<out Debug.MemoryInfo>? {
        return getActivityManager(context).getProcessMemoryInfo(pids)
    }

    fun getAppMemorySize(context: Context, packageName: String): Long {
        val runningAppProcessInfoList = ZAppUtil.getAppProcess(context, packageName)
        var memorySize: Long = 0L
        runningAppProcessInfoList
                .asSequence()
                .mapNotNull { getProcessMemoryInfo(context, intArrayOf(it.pid)) }
                .filter { it.isNotEmpty() }
                .forEach { memorySize = memorySize.plus(it[0].dalvikPrivateDirty) }
        return memorySize
    }

    /**
     * Description : 杀指定包名所在的进程
     * [context] 上下文
     * [packageName] 包名
     * @author zaze
     * @version 2017/5/22 - 下午3:32 1.0
     */
    fun killAppProcess(context: Context, packageName: String) {
        val activityManager = getActivityManager(context)
        activityManager.killBackgroundProcesses(packageName)
        val processInfoList = getAppProcess(context, packageName);
        for (processInfo in processInfoList) {
            Process.killProcess(processInfo.pid)
        }
    }

    // --------------------------------------------------
    /**
     * 清理data数据
     * [packageName] packageName
     */
    fun clearAppData(context: Context, packageName: String) {
        if (ZCommand.isRoot()) {
            ZCommand.execRootCmd("pm clear " + packageName)
        } else {
            ZFileUtil.deleteFile("/data/data/" + packageName)
            killAppProcess(context, packageName)
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    fun startApplication(context: Context, packageName: String, bundle: Bundle? = null) {
        val packageInfo = getPackageInfo(context, packageName)
        if (packageInfo != null) {
            if (null == context.packageManager.getLaunchIntentForPackage(packageName)) {
                ZTipUtil.toast(context, "($packageName)不可直接打开!")
            }
            // 启动应用程序对应的Activity
            val apps = queryIntentActivities(context, packageName)
            var resolveInfo: ResolveInfo? = null
            try {
                resolveInfo = apps.iterator().next()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (resolveInfo != null) {
                val className = resolveInfo.activityInfo.name
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val componentName = ComponentName(packageName, className)
                intent.component = componentName
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent, bundle)
            }
        } else {
            ZTipUtil.toast(context, "($packageName)未安装!")
        }
    }

    fun startApplicationSimple(context: Context, packageName: String, bundle: Bundle? = null) {
        if (!isInstalled(context, packageName)) {
            ZTipUtil.toast(context, "($packageName)未安装!")
            return
        }
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            ZTipUtil.toast(context, "($packageName)不可打开!")
        } else {
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ZActivityUtil.startActivity(context, intent)
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    private fun getActivityManager(context: Context): ActivityManager {
        return context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

}