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
import com.zaze.utils.SignaturesUtil
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
) {
    // --------------------------------------------------
    val sourceDir: String?
        get() {
            return applicationInfo?.sourceDir
        }

    val sharedLibraryFiles: Array<String>?
        get() {
            return applicationInfo?.sharedLibraryFiles
        }
    val nativeLibraryDir: String?
        get() {
            return applicationInfo?.nativeLibraryDir
        }

    val publicSourceDir: String?
        get() {
            return applicationInfo?.publicSourceDir
        }

    @Transient
    @Expose(serialize = false, deserialize = false)
    private var appIcon: Bitmap? = null

    fun updateAppIcon(bitmap: Bitmap) {
        appIcon = bitmap
    }

    fun getAppIcon(context: Context? = null): Bitmap? {
        if (appIcon == null && context != null) {
            // 默认Icon 不赋值，每次都从ApplicationManager缓存中获取
            return ApplicationManager.getAppIconHasDefault(context, packageName)
        }
        return appIcon
    }

    @Transient
    @Expose(serialize = false, deserialize = false)
    private var packageInfo: PackageInfo? = null
    fun getPackageInfo(context: Context): PackageInfo? {
        return when {
            packageInfo != null -> {
                packageInfo
            }
            isInstalled -> {
                packageInfo = AppUtil.getPackageInfo(context, packageName)
                packageInfo
            }
            else -> {
                null
            }
        }
    }

    @Transient
    @Expose(serialize = false, deserialize = false)
    private var applicationInfo: ApplicationInfo? = null
    fun getApplicationInfo(context: Context): ApplicationInfo? {
        return when {
            applicationInfo != null -> {
                applicationInfo
            }
            isInstalled -> {
                applicationInfo = AppUtil.getApplicationInfo(context, packageName)
                applicationInfo
            }
            else -> {
                null
            }
        }
    }

    companion object {
        fun empty(packageName: String): AppShortcut {
            return AppShortcut(packageName = packageName)
        }

        @JvmStatic
        fun transform(context: Context, packageInfo: PackageInfo): AppShortcut {
            val applicationInfo: ApplicationInfo? = packageInfo.applicationInfo
            val appShortcut = AppShortcut(
                packageName = packageInfo.packageName,
                versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode
                } else {
                    packageInfo.versionCode.toLong()
                },
                versionName = packageInfo.versionName,
                appName = applicationInfo?.let {
                    context.packageManager.getApplicationLabel(it).toString()
                },
                isInstalled = applicationInfo != null,
                flags = applicationInfo?.flags ?: 0,
                uid = applicationInfo?.uid ?: 0,
                firstInstallTime = packageInfo.firstInstallTime,
                lastUpdateTime = packageInfo.lastUpdateTime,
            )
            appShortcut.packageInfo = packageInfo
            appShortcut.applicationInfo = applicationInfo
//            ZLog.i(ZTag.TAG, "appShortcut: $appShortcut")
            return appShortcut
        }
    }

    fun isSystemApp(): Boolean {
        return flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    fun getSignatures(context: Context) {
        SignaturesUtil.getSignatures(object : ContextWrapper(context) {
            override fun getPackageName(): String {
                return this@AppShortcut.packageName
            }
        }, "MD5")
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