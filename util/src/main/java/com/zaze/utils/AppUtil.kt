package com.zaze.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.InstallSourceInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.Process
import android.util.Pair
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.zaze.utils.compress.ZipUtil
import com.zaze.utils.ext.isAAB
import com.zaze.utils.ext.isSystemApp
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File


/**
 * Description : App Util
 * @author : ZAZE
 * @version : 2017-05-27 - 17:23
 */
object AppUtil {

    private var activityManager: ActivityManager? = null

    /**
     * [context] context
     * [packageName] null 使用context 的包名
     * @return 应用版本名
     */
    @JvmStatic
    @JvmOverloads
    fun getAppVersionName(context: Context, packageName: String = context.packageName): String {
        return getPackageInfo(context, packageName)?.versionName ?: ""
    }

    /**
     * [context]
     * [packageName] null 使用context 的包名
     * @return 应用版本号
     */
    @JvmStatic
    @JvmOverloads
    fun getAppVersionCode(context: Context, packageName: String = context.packageName): Int {
        return getPackageInfo(context, packageName)?.versionCode ?: 0
    }

    /**
     * 获取app名
     * [context]
     * [packageName]
     * [defaultName]
     * @return app名
     */
    @JvmStatic
    @JvmOverloads
    fun getAppName(
        context: Context, packageName: String = context.packageName, defaultName: String = "未知"
    ): String {
        val applicationInfo = getApplicationInfo(context, packageName)
        return if (applicationInfo == null) {
            defaultName
        } else {
            context.packageManager.getApplicationLabel(applicationInfo).toString()
        }
    }

    /**
     * [context]
     * [packageName]
     * @return 应用图标
     */
    @JvmStatic
    @JvmOverloads
    fun getAppIcon(context: Context, packageName: String = context.packageName): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_DEBUG, e.message)
            null
        }
    }

    fun getAppIcon(
        context: Context, packageName: String = context.packageName, iconDpi: Int
    ): Drawable? {
        val applicationInfo = getApplicationInfo(context, packageName) ?: return null
        val resources = try {
            context.packageManager.getResourcesForApplication(applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return if (resources != null && applicationInfo.icon > 0) {
            getAppIcon(resources, applicationInfo.icon, iconDpi)
        } else {
            null
        }
    }

    /**
     * [resources] resources
     * [iconId] iconId
     * [iconDpi] iconDpi
     * use ResourcesCompat.getDrawableForDensity(resources, iconId, iconDpi, null)
     * @return 应用图标
     */
    @JvmStatic
    fun getAppIcon(resources: Resources, iconId: Int, iconDpi: Int): Drawable? {
        return try {
            return ResourcesCompat.getDrawableForDensity(resources, iconId, iconDpi, null)
        } catch (e: Throwable) {
            null
        }
    }

    /**
     * [context] context
     * [packageName] packageName
     * @author zaze
     * @version 2017/5/31 - 下午3:40 1.0
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    @JvmOverloads
    fun getApplicationInfo(
        context: Context, packageName: String? = null, flag: Long = 0
    ): ApplicationInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getApplicationInfo(
                    packageName ?: context.packageName, ApplicationInfoFlags.of(flag)
                )
            } else {
                context.packageManager.getApplicationInfo(
                    packageName ?: context.packageName, flag.toInt()
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_ABOUT_APP, "没有找到应用信息 : $packageName")
            null
        }
    }

    fun getResourcesForApplication(
        context: Context,
        applicationInfo: ApplicationInfo?
    ): Resources? {
        applicationInfo ?: return null
        return try {
            context.packageManager.getResourcesForApplication(applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * Description : 获取对应包名的PackageInfo
     * [context] context
     * [packageName] 包名
     * @author zaze
     * @version 2017/5/31 - 下午3:46 1.0
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun getPackageInfo(
        context: Context, packageName: String, flag: Int = 0
    ): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    packageName, PackageManager.PackageInfoFlags.of(flag.toLong())
                )
            } else {
                context.packageManager.getPackageInfo(packageName, flag)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            ZLog.e(ZTag.TAG_DEBUG, "PackageManager.NameNotFoundException : ${packageName}")
            null
        }
    }

    /**
     * Description : 获取apk文件的PackageInfo
     * [context] context
     * [fileName] fileName
     * @author zaze
     * @version 2017/8/26 - 下午3:23 1.0
     */
    @Suppress("DEPRECATION")
    fun getPackageArchiveInfo(
        context: Context,
        archiveFilePath: String?,
        flags: Int = 0
    ): PackageInfo? {
        if (archiveFilePath.isNullOrEmpty()) {
            return null
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageArchiveInfo(
                archiveFilePath,
                PackageManager.PackageInfoFlags.of(flags.toLong())
            )
        } else {
            context.packageManager.getPackageArchiveInfo(archiveFilePath, flags)
        }
    }

    @JvmStatic
    fun findSystemAppByBroadcastReceivers(
        context: Context, action: String
    ): Pair<String, Resources>? {
        val pm = context.packageManager
        for (info in pm.queryBroadcastReceivers(Intent(action), 0)) {
            if (info.activityInfo?.applicationInfo?.isSystemApp == true) {
                val packageName = info.activityInfo.packageName
                try {
                    val res = pm.getResourcesForApplication(packageName)
                    return Pair.create(packageName, res)
                } catch (e: PackageManager.NameNotFoundException) {
                    ZLog.w(ZTag.TAG_DEBUG, "Failed to find resources for $packageName")
                }
            }
        }
        return null
    }


    @JvmStatic
    fun getPackagesForUid(context: Context, uid: Int): Array<String>? {
        return context.packageManager.getPackagesForUid(uid)
    }

    @JvmStatic
    fun getNameForUid(context: Context, uid: Int): String? {
        return context.packageManager.getNameForUid(uid)
    }

    /**
     * 获取启动信息
     */
    @JvmStatic
    fun queryMainIntentActivities(context: Context, packageName: String): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        mainIntent.`package` = packageName
        return context.packageManager.queryIntentActivities(
            mainIntent, PackageManager.MATCH_DEFAULT_ONLY
        )
    }

    fun getAppMetaData(context: Context, packageName: String): Bundle? {
        return try {
            context.packageManager.getApplicationInfo(
                packageName, PackageManager.GET_META_DATA
            ).metaData
        } catch (e: Exception) {
            null
        }
    }

    fun getSignatures(
        context: Context,
        packageName: String = context.packageName
    ): Array<Signature>? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageInfo(
                    context,
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )?.signingInfo?.apkContentsSigners
            } else {
                getPackageInfo(
                    context,
                    packageName,
                    PackageManager.GET_SIGNATURES
                )?.signatures
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getApkFileSignatures(
        context: Context,
        filePath: String?,
    ): Array<Signature>? {
        if (!FileUtil.exists(filePath)) return null
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageArchiveInfo(
                    context,
                    filePath,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )?.signingInfo?.apkContentsSigners
            } else {
                getPackageArchiveInfo(
                    context,
                    filePath,
                    PackageManager.GET_SIGNATURES
                )?.signatures
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getInstallerPackageName(context: Context, packageName: String): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val installSourceInfo = getInstallSourceInfo(context, packageName)
                installSourceInfo?.installingPackageName
            } else {
                context.packageManager.getInstallerPackageName(packageName)
            }
        } catch (e: Throwable) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getInstallSourceInfo(context: Context, packageName: String): InstallSourceInfo? {
        return try {
            context.packageManager.getInstallSourceInfo(packageName)
        } catch (e: Throwable) {
            null
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

    @JvmStatic
    fun getInstalledPackages(context: Context, flag: Int = 0): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(flag)
    }

    @JvmStatic
    fun getInstalledApplications(context: Context, flag: Int = 0): List<ApplicationInfo> {
        return context.packageManager.getInstalledApplications(flag)
    }


    // --------------------------------------------------
    // --------------------------------------------------

    @JvmStatic
    fun isActivityExist(context: Context, intent: Intent?): Boolean {
        return if (intent != null) {
            !context.packageManager.queryIntentActivities(intent, 0).isEmpty()
        } else {
            false
        }
    }

    /**
     * Description : app 是否在运行中
     * [context] 上下文
     * [packageName] 包名
     * @author zaze
     * @version 2017/5/22 - 下午3:32 1.0
     */
    @JvmStatic
    fun isAppRunning(context: Context, packageName: String): Boolean {
        return if (!isInstalled(context, packageName)) {
            false
        } else if (getAppProcess(context, packageName).isEmpty()) {
            getAppPid(packageName) > 0
        } else {
            true
        }
    }

    /**
     * 是否是系统应用
     * [context] context
     * [intent] intent
     */
    @JvmStatic
    fun isSystemApp(context: Context, intent: Intent): Boolean {
        val componentName = intent.component
        var packageName: String? = null
        if (componentName == null) {
            val info =
                context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
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
    @JvmStatic
    fun isSystemApp(context: Context, packageName: String?): Boolean {
        return getApplicationInfo(context, packageName)?.isSystemApp ?: false
    }

    /**
     * 是否是系统应用
     * [applicationInfo] applicationInfo
     */
    @JvmStatic
    @Deprecated("use {@link ApplicationInfo.isSystemApp}")
    fun isSystemApp(applicationInfo: ApplicationInfo?): Boolean {
        return applicationInfo?.isSystemApp ?: false
    }


    /**
     * Description : 应用是否安装
     * [context]
     * [packageName]
     * @author zaze
     * @version 2017/5/31 - 下午3:47 1.0
     */
    @JvmStatic
    fun isInstalled(context: Context, packageName: String): Boolean {
        return getApplicationInfo(context, packageName) != null
    }
    // --------------------------------------------------

    @JvmStatic
    fun getAppPid(packageName: String): Int {
        ZLog.i(ZTag.TAG_DEBUG, "getAppPid : $packageName")
//        if (ZCommand.isCommandExists("grep")) {
        val result = ZCommand.execCmdForRes("ps -A |grep $packageName")
        if (ZCommand.isSuccess(result) && result.successList.size > 0) {
            val message = result.successList[0]
            ZLog.i(ZTag.TAG_DEBUG, "getAppPid : $message")
            val fields =
                message.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (fields.size > 1) {
                return ZStringUtil.parseInt(fields[1])
            }
        }
//        }
        return 0
    }

    /**
     * Description : 返回 应用所在进程
     * [context] context
     * [packageName] packageName
     * @author zaze
     * @version 2017/5/31 - 下午2:59 1.0
     */
    @Deprecated("未Root情况下, Android5.0以后只能获取到自己")
    @JvmStatic
    fun getAppProcess(
        context: Context, packageName: String
    ): ArrayList<ActivityManager.RunningAppProcessInfo> {
        val activityManager = getActivityManager(context)
        val list = ArrayList<ActivityManager.RunningAppProcessInfo>()
        if (activityManager.runningAppProcesses != null) {
            activityManager.runningAppProcesses.filterTo(list) { it.pkgList.contains(packageName) }
        }
        return list
    }

    /**
     * Description : MemoryInfo
     * @author zaze
     * @version 2017/10/9 - 下午1:51 1.0
     */
    @JvmStatic
    fun getProcessMemoryInfo(context: Context, pids: IntArray): Array<Debug.MemoryInfo>? {
        return getActivityManager(context).getProcessMemoryInfo(pids)
    }

    @JvmStatic
    fun getAppMemorySize(context: Context, packageName: String): Long {
        val runningAppProcessInfoList = AppUtil.getAppProcess(context, packageName)
        var memorySize = 0L
        runningAppProcessInfoList.asSequence()
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
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun killAppProcess(context: Context, packageName: String) {
        getActivityManager(context).killBackgroundProcesses(packageName)
        val processInfoList = getAppProcess(context, packageName)
        for (processInfo in processInfoList) {
            if (processInfo.pid != Process.myPid()) {
                Process.killProcess(processInfo.pid)
            }
        }
        Process.killProcess(Process.myPid())
    }

    // --------------------------------------------------
    /**
     * 清理data数据
     * [packageName] packageName
     */
    @JvmStatic
    fun clearAppData(context: Context, packageName: String) {
        if (ZCommand.isRoot()) {
            ZCommand.execRootCmd("pm clear $packageName")
        } else {
            FileUtil.deleteFile("${context.filesDir.path}/data/$packageName")
            killAppProcess(context, packageName)
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    @JvmOverloads
    fun startApplication(
        context: Context, packageName: String, bundle: Bundle? = null, needToast: Boolean = true
    ): Boolean {
        if (!isInstalled(context, packageName)) {
            if (needToast) {
                ToastUtil.toast(context, "$packageName 未安装!")
            }
            return false
        }
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent == null) {
            if (needToast) {
                ToastUtil.toast(context, "$packageName 不可打开!")
            }
            return false
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        context.startActivity(launchIntent, bundle)
        return true
    }

    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    fun getActivityManager(context: Context): ActivityManager {
        return activityManager
            ?: (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).also {
                activityManager = it
            }
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * Description : 安装应用
     *
     * [context] 上下文
     * [filePath] 文件绝对路径
     *
     * val intent = Intent(Intent.ACTION_VIEW)
     * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
     * intent.addCategory(Intent.CATEGORY_DEFAULT)
     * val uri: Uri
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
     * uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
     * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
     * } else {
     * uri = Uri.fromFile(file)
     * }
     * intent.setDataAndType(uri, "application/vnd.android.package-archive")
     * context.startActivity(intent)
     *
     * @author zaze
     * @version 2017/5/31 - 下午2:54 1.0
     */
    @JvmStatic
    @Deprecated("use FileProvider")
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

//    fun install(context: Context, file: File) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        val uri: Uri
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        } else {
//            uri = Uri.fromFile(file)
//        }
//        intent.setDataAndType(uri, "application/vnd.android.package-archive")
//        context.startActivity(intent)
//    }


    /**
     * Description : 卸载应用
     * [context] 上下文
     * [packageName] 需要卸载应用的包名
     * @author zaze
     * @version 2017/5/31 - 下午2:57 1.0
     */
    @JvmStatic
    @JvmOverloads
    fun unInstall(context: Context, packageName: String = context.packageName) {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始卸载 $packageName")
        val uninstallIntent = Intent()
        uninstallIntent.action = Intent.ACTION_DELETE
        uninstallIntent.data = Uri.parse("package:$packageName")
        uninstallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(uninstallIntent)
    }

    /**
     * 静默安装
     * [filePath] 文件绝对路径
     */
    @JvmStatic
    fun installSilent(filePath: String): Boolean {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默安装 $filePath")
        return if (ZCommand.isSuccess(ZCommand.execRootCmdForRes("pm install -r $filePath"))) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装成功!")
            true
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默安装失败!")
            false
        }
    }

    /**
     * 静默卸载
     * [packageName] 报名
     */
    @JvmStatic
    fun unInstallSilent(packageName: String): Boolean {
        ZLog.i(ZTag.TAG_ABOUT_APP, "开始静默卸载 $packageName")
        return if (ZCommand.isSuccess(ZCommand.execRootCmdForRes("pm uninstall $packageName"))) {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载成功!")
            true
        } else {
            ZLog.i(ZTag.TAG_ABOUT_APP, "静默卸载失败!")
            false
        }
    }
}