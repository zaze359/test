package com.zaze.demo.feature.applications

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.google.gson.annotations.Expose
import com.zaze.utils.AppUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.SignaturesUtil
import com.zaze.utils.ext.getLabel
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.lang.Exception
import java.util.*


/**
 * Description : 应用快捷信息(汇总最新的 Applicaiton 和 packageInfo)
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:26
 */
data class AppShortcut(

    val appName: String? = null,
    val packageName: String = "",
    val versionCode: Long = 0,
    val versionName: String? = null,
    val uid: Int = 0,
    val flags: Int = 0,
    val isInstalled: Boolean = false,
    val firstInstallTime: Long = 0,
    val lastUpdateTime: Long = 0,
    val installerPackageName: String? = null,
    @Transient
    @Expose(serialize = false, deserialize = false)
    val packageInfo: PackageInfo? = null,
    @Transient
    @Expose(serialize = false, deserialize = false)
    val applicationInfo: ApplicationInfo? = null
) {
    // --------------------------------------------------
    var isLocalFile: Boolean = false

    val enable: Boolean get() = applicationInfo?.enabled ?: false

    val sourceDir: String? = applicationInfo?.sourceDir

    val apkSize: Long
        get() = FileUtil.getFileSize(applicationInfo?.sourceDir)

    @Transient
    @Expose(serialize = false, deserialize = false)
    private var appIcon: Bitmap? = null

    fun updateAppIcon(bitmap: Bitmap) {
        appIcon = bitmap
    }

    fun getAppIcon(context: Context? = null): Bitmap? {
        if (appIcon == null && context != null) {
            // 默认不赋值，每次都从ApplicationManager缓存中获取
            return ApplicationManager.getAppIconHasDefault(context, packageName)
        }
        return appIcon
    }

    companion object {
        fun empty(packageName: String): AppShortcut {
            return AppShortcut(packageName = packageName)
        }

        @JvmStatic
        fun create(context: Context, packageInfo: PackageInfo): AppShortcut {
            val applicationInfo = packageInfo.applicationInfo
            val isInstalled = applicationInfo != null
            val appShortcut = AppShortcut(
                packageName = packageInfo.packageName,
                versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    packageInfo.versionCode.toLong()
                },
                versionName = packageInfo.versionName,
                appName = applicationInfo?.getLabel(context),
                isInstalled = isInstalled,
                flags = applicationInfo?.flags ?: 0,
                uid = applicationInfo?.uid ?: 0,
                firstInstallTime = packageInfo.firstInstallTime,
                lastUpdateTime = packageInfo.lastUpdateTime,
                installerPackageName = if (isInstalled) AppUtil.getInstallerPackageName(
                    context,
                    packageInfo.packageName
                ) else null,
                packageInfo = packageInfo,
                applicationInfo = applicationInfo
            )
//            ZLog.i(ZTag.TAG, "appShortcut: $appShortcut")
            return appShortcut
        }
    }

    fun isSystemApp(): Boolean {
        return flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun getSignatures(context: Context): String? {
        return if (!isLocalFile) {
            SignaturesUtil.getSignatures(
                AppUtil.getSignatures(context, this.packageName),
                "MD5"
            )
        } else {
            SignaturesUtil.getSignatures(
                AppUtil.getApkFileSignatures(context, this.sourceDir),
                "MD5"
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun queryStorageStats(context: Context) {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageStatsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        storageManager.storageVolumes.forEach { volume ->
            val uuid = volume.uuid?.let {
                UUID.fromString(it)
            } ?: StorageManager.UUID_DEFAULT
            val storageStats = storageStatsManager.queryStatsForPackage(
                uuid,
                packageName,
                android.os.Process.myUserHandle()
            )
            ZLog.i(
                ZTag.TAG,
                "$appName ${volume.getDescription(context)} appBytes: ${storageStats.appBytes}; cacheBytes: ${storageStats.cacheBytes}; dataBytes: ${storageStats.dataBytes}"
            )
        }
    }

    fun getResource(context: Context): Resources? {
        return applicationInfo?.let {
            try {
                context.packageManager.getResourcesForApplication(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}