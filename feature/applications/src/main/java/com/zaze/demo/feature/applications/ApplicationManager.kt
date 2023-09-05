package com.zaze.demo.feature.applications

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.TextUtils
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.LruCache
import com.zaze.common.base.BaseApplication
import com.zaze.common.util.FileProviderHelper
import com.zaze.utils.AppUtil
import com.zaze.utils.BmpUtil
import com.zaze.utils.ext.BitmapExt
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
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
object ApplicationManager : AppChangeListener {
    val invariantDeviceProfile = InvariantDeviceProfile()

    private val appObserver = ArrayList<AppChangeListener>()

    /**
     * 默认logo缓存
     */
    private var defaultLogoBmpRef: SoftReference<Bitmap?>? = null

    /**
     * 应用图标bitmap 缓存
     * 占总内存的 1/8，KB
     */
//    private val BITMAP_CACHE = LruCache<String, Bitmap?>(60)
    private val BITMAP_CACHE =
        object : LruCache<String, Bitmap?>((Runtime.getRuntime().maxMemory() / 1024).toInt() / 8) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                if (value == null) return super.sizeOf(key, null)
                // 返回bitmap的内存大小
                return value.rowBytes * value.height / 1024
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: String?,
                oldValue: Bitmap?,
                newValue: Bitmap?
            ) {
                super.entryRemoved(evicted, key, oldValue, newValue)
            }
        }

    /**
     * 应用信息缓存
     */
    private val SHORTCUT_CACHE: MutableMap<String, AppShortcut> = HashMap()
    private var allAppInitialized = false

    // --------------------------------------------------
    private fun saveShortcutToCache(packageName: String, appShortcut: AppShortcut) {
        synchronized(SHORTCUT_CACHE) {
            SHORTCUT_CACHE[packageName] = appShortcut
        }
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
        synchronized(SHORTCUT_CACHE) {
            SHORTCUT_CACHE.clear()
        }
    }

    fun clearCache(packageName: String) {
        clearAppCache(packageName)
    }

    private fun clearAppCache(packageName: String) {
        synchronized(SHORTCUT_CACHE) {
            SHORTCUT_CACHE.remove(packageName)
        }
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
    fun getAppShortcutFormApk(context: Context, apkFilePath: String): AppShortcut? {
        val packageInfo = AppUtil.getPackageArchiveInfo(context, apkFilePath)
        return if (packageInfo != null) {
            packageInfo.applicationInfo.sourceDir = apkFilePath
            packageInfo.applicationInfo.publicSourceDir = apkFilePath
            val appShortcut = AppShortcut.create(context, packageInfo)
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
            AppShortcut.create(BaseApplication.getInstance(), packageInfo)
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
        return getAppIcon(context, packageName) ?: getAppDefaultLogo()
    }

    fun getAppIcon(context: Context, packageName: String): Bitmap? {
        if (TextUtils.isEmpty(packageName)) {
            return null
        }
        var bitmap = BITMAP_CACHE[packageName]
        if (bitmap != null) {
            return bitmap
        }
        val appShortcut = getAppShortcut(packageName)
        var appIcon: Drawable? = null
        val applicationInfo = appShortcut.applicationInfo
        if (applicationInfo != null) {
            val resources = getAppResources(applicationInfo)
            if (resources != null) {
                appIcon = getFullResIcon(resources, applicationInfo.icon)
            }
        }
        if (appIcon != null) {
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
            bitmap = BitmapExt.decodeToBitmap(
                invariantDeviceProfile.iconBitmapSize,
                invariantDeviceProfile.iconBitmapSize
            ) {
                BitmapFactory.decodeResource(
                    BaseApplication.getInstance().resources,
                    R.drawable.ic_app_default,
                    it
                )
            }
//            bitmap = formatIcon(
//                getFullResIcon(
//                    BaseApplication.getInstance().resources,
//                    R.drawable.ic_app_default
//                )
//            )
            defaultLogoBmpRef = SoftReference(bitmap)
        }
        return bitmap
    }

    private fun getFullResIcon(resources: Resources, iconId: Int): Drawable? {
        return AppUtil.getAppIcon(resources, iconId, invariantDeviceProfile.fillResIconDpi)
    }

    private fun formatIcon(drawable: Drawable?): Bitmap? {
        return BmpUtil.drawable2Bitmap(drawable, invariantDeviceProfile.iconBitmapSize)
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
        return initAllShortcuts().filter {
            it.value.isInstalled
        }
    }

    fun getAllApps(): Map<String, AppShortcut> {
        return initAllShortcuts()
    }

    fun initAllShortcuts(): Map<String, AppShortcut> {
        synchronized(SHORTCUT_CACHE) {
            if (SHORTCUT_CACHE.isEmpty() || !allAppInitialized) {
                AppUtil.getInstalledPackages(BaseApplication.getInstance(), 0).forEach {
                    initAppShortcut(it.packageName, it)
                }
                allAppInitialized = true
            }
            return SHORTCUT_CACHE
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
            uri = FileProviderHelper.getUriForFile(context, file)
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

    // ------------------------------------------

    fun addAppObserver(observer: AppChangeListener) {
        synchronized(appObserver) {
            appObserver.add(observer)
        }
    }

    fun removeAppObserver(observer: AppChangeListener) {
        synchronized(appObserver) {
            appObserver.remove(observer)
        }
    }

    override fun afterAppAdded(packageName: String) {
        ZLog.i(ZTag.TAG, "添加应用 : $packageName")
        initAppShortcut(packageName)
        synchronized(appObserver) {
            appObserver.forEach {
                it.afterAppAdded(packageName)
            }
        }
    }

    override fun afterAppReplaced(packageName: String) {
        ZLog.i(ZTag.TAG, "替换应用 : $packageName")
        initAppShortcut(packageName)
        synchronized(appObserver) {
            appObserver.forEach {
                it.afterAppReplaced(packageName)
            }
        }
    }

    override fun afterAppRemoved(packageName: String) {
        ZLog.i(ZTag.TAG, "卸载成功$packageName")
        clearCache(packageName)
        synchronized(appObserver) {
            appObserver.forEach {
                it.afterAppRemoved(packageName)
            }
        }
    }
}

interface AppChangeListener {
    fun afterAppAdded(packageName: String)
    fun afterAppReplaced(packageName: String)
    fun afterAppRemoved(packageName: String)
}