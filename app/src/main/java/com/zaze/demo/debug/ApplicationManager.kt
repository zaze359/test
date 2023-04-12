package com.zaze.demo.debug

import android.content.Context
import android.content.Intent
import com.zaze.utils.BmpUtil.drawable2Bitmap
import android.graphics.Bitmap
import android.text.TextUtils
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.LruCache
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.zaze.common.base.BaseApplication
import com.zaze.demo.R
import com.zaze.utils.AppUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.lang.Exception
import java.lang.ref.SoftReference
import java.util.HashMap

/**
 * Description : 对应用信息进行管理
 * 当应用发生变化时清空缓存, 否则有些从内存缓存中读取;
 * 仅当获取不到应用信息时尝试从文件缓存中读取(用于特殊场景).
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:22
 */
object ApplicationManager {
    val invariantDeviceProfile = InvariantDeviceProfile()

    /**
     * 默认logo缓存
     */
    private var defaultLogoBmpRef: SoftReference<Bitmap?>? = null

    /**
     * 应用图标bitmap 缓存
     */
    private val BITMAP_CACHE = LruCache<String, Bitmap?>(60)

    /**
     * 应用信息缓存
     */
    private val SHORTCUT_CACHE: MutableMap<String, AppShortcut> = HashMap()

    private var allAppInitialized = false

    // --------------------------------------------------
    private fun saveShortcutToCache(packageName: String, appShortcut: AppShortcut) {
        SHORTCUT_CACHE[packageName] = appShortcut
    }

    /**
     * 从引用中获取AppShortcut
     *
     * @param packageName packageName
     * @return appShortcut or null
     */
    private fun getShortcutFromCache(packageName: String): AppShortcut? {
        return SHORTCUT_CACHE[packageName]
    }

    fun clearAllCache() {
        BITMAP_CACHE.evictAll()
        SHORTCUT_CACHE.clear()
    }

    fun clearCache(packageName: String) {
        clearAppCache(packageName)
    }

    private fun clearAppCache(packageName: String) {
        SHORTCUT_CACHE.remove(packageName)
        BITMAP_CACHE.remove(packageName)
    }
    // --------------------------------------------------
    /**
     * 根据包名获取应用信息
     * 优先读取内存缓存
     * 其次系统中读取
     *
     * @param packageName packageName
     * @return AppShortcut
     */
    fun getAppShortcut(packageName: String): AppShortcut {
        var appShortcut = getShortcutFromCache(packageName)
        if (appShortcut == null) {
            appShortcut = initAppShortcut(packageName)
        }
        return appShortcut
    }

    /**
     * 根据包名获取应用信息
     * 优先读取内存缓存
     * 其次系统中读取
     *
     * @param apkFilePath apkFilePath
     * @return AppShortcut
     */
    fun getAppShortcutFormApk(apkFilePath: String): AppShortcut? {
        val packageInfo = AppUtil.getPackageArchiveInfo(BaseApplication.getInstance(), apkFilePath)
        return if (packageInfo != null) {
            packageInfo.applicationInfo.sourceDir = apkFilePath
            packageInfo.applicationInfo.publicSourceDir = apkFilePath
            val appShortcut = AppShortcut.transform(BaseApplication.getInstance(), packageInfo)
            appShortcut
        } else {
            null
        }
    }

    /**
     * 初始化应用缓存信息
     *
     * @param packageName packageName
     * @return AppShortcut
     */
    private fun initAppShortcut(
        packageName: String,
        packageInfo: PackageInfo? = AppUtil.getPackageInfo(
            BaseApplication.getInstance(),
            packageName,
            0
        )
    ): AppShortcut {
        val appShortcut = if (packageInfo == null) {
            AppShortcut.empty(packageName)
        } else {
            AppShortcut.transform(BaseApplication.getInstance(), packageInfo)
        }
        saveShortcutToCache(appShortcut.packageName, appShortcut)
        return appShortcut
    }
    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * [packageName] packageName
     *
     * @return 应用图标
     */
    fun getAppIconHasDefault(context: Context, packageName: String): Bitmap? {
        if (TextUtils.isEmpty(packageName)) {
            return null
        }
        var bitmap = BITMAP_CACHE[packageName]
        if (bitmap != null) {
            return bitmap
        }
        val appShortcut = getAppShortcut(packageName)
        var appIcon: Drawable? = null
        val applicationInfo = appShortcut.getApplicationInfo(context)
        if (applicationInfo != null) {
            val resources = getAppResources(applicationInfo)
            if (resources != null) {
                appIcon = getFullResIcon(resources, applicationInfo.icon)
            }
        }
        if (appIcon == null) {
            bitmap = getAppDefaultLogo()
        } else {
            bitmap = formatIcon(appIcon)
            BITMAP_CACHE.put(packageName, bitmap)
        }
        return bitmap
    }

    fun getAppResources(application: ApplicationInfo?): Resources? {
        return if (application == null) {
            null
        } else try {
            BaseApplication.getInstance().packageManager.getResourcesForApplication(application)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * 获取应用默认logo
     *
     * @return Bitmap
     */
    fun getAppDefaultLogo(): Bitmap? {
        var bitmap = defaultLogoBmpRef?.get()
        if (bitmap == null) {
            bitmap = formatIcon(
                getFullResIcon(
                    BaseApplication.getInstance().resources,
                    R.mipmap.ic_launcher
                )
            )
            defaultLogoBmpRef = SoftReference(bitmap)
        }
        return bitmap
    }

    private fun getFullResIcon(resources: Resources, iconId: Int): Drawable? {
        return try {
            ResourcesCompat.getDrawableForDensity(
                resources,
                iconId,
                invariantDeviceProfile.fillResIconDpi,
                null
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun formatIcon(drawable: Drawable?): Bitmap? {
        return drawable2Bitmap(drawable, invariantDeviceProfile.iconBitmapSize)
    }
    // --------------------------------------------------
    /**
     * [packageName] packageName packageName
     *
     * @return 应用图标（默认ic_launcher）
     */
    fun getAppNameHasDefault(packageName: String?, defaultName: String?): String? {
        if (packageName == null || packageName.isEmpty()) {
            return defaultName
        }
        var name = getAppShortcut(packageName).appName
        if (TextUtils.isEmpty(name)) {
            ZLog.e(ZTag.TAG_DEBUG, "未获取到应用名($packageName), 使用默认($defaultName)")
            name = defaultName
        }
        //        XHLog.i(LcTag.TAG_DEBUG, "获取到应用名(%s) : (%s)", packageName, name);
        return name
    }

    fun getInstallApps(): Map<String, AppShortcut> {
        initAllShortcuts()
        return SHORTCUT_CACHE.filter {
            it.value.isInstalled
        }
    }

    fun getAllApps(): Map<String, AppShortcut> {
        initAllShortcuts()
        return SHORTCUT_CACHE.toMap()
    }

    fun initAllShortcuts() {
        synchronized(SHORTCUT_CACHE) {
            if (SHORTCUT_CACHE.isEmpty() || !allAppInitialized) {
                AppUtil.getInstalledPackages(BaseApplication.getInstance(), 0).forEach {
                    initAppShortcut(it.packageName, it)
                }
                allAppInitialized = true
            }
        }
    }

    // --------------------------------------------------
    /**
     * 安装应用
     */
    fun installApp(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * 卸载应用
     */
    fun uninstallApp(context: Context, packageName: String) {
        AppUtil.unInstall(context, packageName)
    }

    /**
     * 打开应用
     */
    fun openApp(context: Context, packageName: String, bundle: Bundle? = null) {
        AppUtil.startApplication(context, packageName, bundle, true)
    }
}