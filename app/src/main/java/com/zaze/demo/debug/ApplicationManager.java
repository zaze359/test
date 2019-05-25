package com.zaze.demo.debug;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.LruCache;

import androidx.core.content.res.ResourcesCompat;

import com.zaze.common.base.BaseApplication;
import com.zaze.demo.R;
import com.zaze.utils.AppUtil;
import com.zaze.utils.BmpUtil;
import com.zaze.utils.FileUtil;
import com.zaze.utils.JsonUtil;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.config.ConfigHelper;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;

/**
 * Description : 对应用信息进行管理
 * 当应用发生变化时清空缓存, 否则有些从内存缓存中读取;
 * 仅当获取不到应用信息时尝试从文件缓存中读取(用于特殊场景).
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:22
 */
public class ApplicationManager {
    private static InvariantDeviceProfile invariantDeviceProfile = new InvariantDeviceProfile();

    /**
     * 记录最近的一个 uid, 仅用于获取uid时使用
     */
    private static ConfigHelper latelyUidFile = ConfigHelper.newInstance(FileUtil.getSDCardRoot() + "/zaze/LatelyUid.stats");

    /**
     * 记录最近一个versionCode
     */
    private static ConfigHelper latelyVersionFile = ConfigHelper.newInstance(FileUtil.getSDCardRoot() + "/zaze/LatelyVersion.stats");

    /**
     * 默认logo缓存
     */
    private static SoftReference<Bitmap> defaultLogoBmpRef;

    /**
     * 默认下载icon缓存
     */
    private static SoftReference<Bitmap> defaultDownloadBmpRef;

    /**
     * 应用图标bitmap 缓存
     */
    private static final LruCache<String, Bitmap> BITMAP_CACHE = new LruCache<>(60);
    /**
     * 应用信息缓存
     */
    private static final LruCache<String, AppShortcut> SHORTCUT_CACHE = new LruCache<>(60);

    // --------------------------------------------------

    private static void saveShortcutToCache(String packageName, AppShortcut appShortcut) {
        SHORTCUT_CACHE.put(packageName, appShortcut);
    }

    /**
     * 从引用中获取AppShortcut
     *
     * @param packageName packageName
     * @return appShortcut or null
     */
    private static AppShortcut getShortcutFromCache(String packageName) {
        return SHORTCUT_CACHE.get(packageName);
    }


    private static class Key {
        private static String getAppNameKey(String packageName) {
            return "am_name_" + packageName;
        }

        private static String getAppUidKey(int uid) {
            return "am_uid_" + uid;
        }

        private static String getAppVersionKey(String packageName) {
            return "am_version_" + packageName;
        }

        private static String getOtherAppPkgKey() {
            return "am_other_pkg";
        }
    }

    public static void clearAllCache() {
        BITMAP_CACHE.evictAll();
        SHORTCUT_CACHE.evictAll();
    }

    public static void clearCache(String packageName) {
        MemoryCacheManager.deleteCache(Key.getOtherAppPkgKey());
        clearAppCache(packageName);
    }

    private static void clearAppCache(String packageName) {
        AppShortcut appShortcut = getShortcutFromCache(packageName);
        if (appShortcut != null) {
            MemoryCacheManager.deleteCache(Key.getAppUidKey(appShortcut.getUid()));
        }
        MemoryCacheManager.deleteCache(Key.getAppNameKey(packageName));
        SHORTCUT_CACHE.remove(packageName);
        BITMAP_CACHE.remove(packageName);
    }

    // --------------------------------------------------

    /**
     * 通过uid获取应用信息
     * 优先读取内存缓存
     * 其次系统中读取
     * 其次读取文件缓存
     *
     * @param uid uid
     * @return 应用信息
     */
    public static AppShortcut getAppShortcutByUid(int uid) {
        String key = Key.getAppUidKey(uid);
        String packageName = MemoryCacheManager.getCache(key);
        AppShortcut appShortcut = null;
        if (TextUtils.isEmpty(packageName)) {
            packageName = AppUtil.getNameForUid(BaseApplication.getInstance(), uid);
            if (!TextUtils.isEmpty(packageName)) {
                packageName = packageName.split(":")[0];
            }
        }
        if (!TextUtils.isEmpty(packageName)) {
            appShortcut = getAppShortcut(packageName);
            if (appShortcut == null) {
                appShortcut = JsonUtil.parseJson(latelyUidFile.getProperty(key), AppShortcut.class);
            } else {
                latelyUidFile.setProperty(key, JsonUtil.objToJson(appShortcut));
                MemoryCacheManager.saveCache(key, packageName);
            }
        }
        return appShortcut;
    }

    /**
     * 根据包名获取应用信息
     * 优先读取内存缓存
     * 其次系统中读取
     *
     * @param packageName packageName
     * @return AppShortcut
     */
    public static AppShortcut getAppShortcut(@NotNull String packageName) {
        AppShortcut appShortcut = getShortcutFromCache(packageName);
        if (appShortcut == null) {
            appShortcut = initAppShortcut(packageName);
        }
        return appShortcut;
    }

    /**
     * 根据包名获取应用信息
     * 优先读取内存缓存
     * 其次系统中读取
     *
     * @param apkFilePath apkFilePath
     * @return AppShortcut
     */
    public static AppShortcut getAppShortcutFormApk(@NotNull String apkFilePath) {
        PackageInfo packageInfo = AppUtil.getPackageArchiveInfo(BaseApplication.getInstance(), apkFilePath);
        if (packageInfo != null) {
            packageInfo.applicationInfo.sourceDir = apkFilePath;
            packageInfo.applicationInfo.publicSourceDir = apkFilePath;
            AppShortcut appShortcut = AppShortcut.transform(BaseApplication.getInstance(), packageInfo);
            appShortcut.setSourceDir(apkFilePath);
            return appShortcut;
        } else {
            return null;
        }
    }

    /**
     * 尝试将数据加载到缓存
     *
     * @param packageName packageName
     */
    public static void tryLoadAppShortcut(String packageName) {
        if (!TextUtils.isEmpty(packageName) && getShortcutFromCache(packageName) == null) {
            initAppShortcut(packageName);
        }
    }

    /**
     * 初始化应用缓存信息
     *
     * @param packageName packageName
     * @return AppShortcut
     */
    private static AppShortcut initAppShortcut(@NotNull final String packageName) {
        PackageInfo packageInfo = AppUtil.getPackageInfo(BaseApplication.getInstance(), packageName, 64);
        AppShortcut appShortcut = null;
        int versionCode = 1;
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
            appShortcut = AppShortcut.transform(BaseApplication.getInstance(), packageInfo);
        }
        if (appShortcut != null) {
            saveShortcutToCache(packageName, appShortcut);
            if (versionCode > 0) {
                final int saveVersionCode = versionCode;
                ThreadManager.getInstance().runInDiskIO(new Runnable() {
                    @Override
                    public void run() {
                        latelyVersionFile.setProperty(Key.getAppVersionKey(packageName), String.valueOf(saveVersionCode));
                    }
                });
            }
        }
        if (appShortcut == null) {
            appShortcut = new AppShortcut();
            appShortcut.setPackageName(packageName);
            appShortcut.setInstalled(false);
        }
        return appShortcut;

    }
    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * [packageName] packageName
     *
     * @return 应用图标
     */
    public static Bitmap getAppIconHasDefault(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        Bitmap bitmap = BITMAP_CACHE.get(packageName);
        if (bitmap != null) {
            return bitmap;
        }

        ApplicationInfo applicationInfo = AppUtil.getApplicationInfo(
                BaseApplication.getInstance(), packageName
        );
        Drawable appIcon = null;
        if (applicationInfo != null) {
            Resources resources;
            try {
                resources = BaseApplication.getInstance().getPackageManager().getResourcesForApplication(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                resources = null;
            }
            if (resources != null) {
                appIcon = getFullResIcon(resources, applicationInfo.icon);
            }
        }
        if (appIcon == null) {
            bitmap = ApplicationManager.getAppDefaultLogo();
        } else {
            bitmap = makeDefaultIcon(appIcon);
            BITMAP_CACHE.put(packageName, bitmap);
        }
        return bitmap;
    }

    /**
     * 获取应用默认logo
     *
     * @return Bitmap
     */
    public static Bitmap getAppDefaultLogo() {
        Bitmap bitmap = null;
        if (defaultLogoBmpRef != null) {
            bitmap = defaultLogoBmpRef.get();
        }
        if (bitmap == null) {
            bitmap = makeDefaultIcon(getFullResIcon(BaseApplication.getInstance().getResources(),
                    R.mipmap.ic_launcher)
            );
            defaultLogoBmpRef = new SoftReference<>(bitmap);
        }
        return bitmap;
    }

    private static Drawable getFullResIcon(Resources resources, int iconId) {
        return ResourcesCompat.getDrawableForDensity(resources, iconId, invariantDeviceProfile.getFillResIconDpi(), null);
    }

    private static Bitmap makeDefaultIcon(Drawable drawable) {
        return BmpUtil.drawable2Bitmap(drawable, invariantDeviceProfile.getIconBitmapSize());
    }

    // --------------------------------------------------

    /**
     * [packageName] packageName packageName
     *
     * @return 应用图标（默认ic_launcher）
     */
    public static String getAppNameHasDefault(String packageName, String defaultName) {
        if (TextUtils.isEmpty(packageName)) {
            return defaultName;
        }
        String name = MemoryCacheManager.getCache(Key.getAppNameKey(packageName));
        if (TextUtils.isEmpty(name)) {
            AppShortcut appShortcut = getAppShortcut(packageName);
            if (appShortcut != null) {
                name = appShortcut.getName();
            }
            if (TextUtils.isEmpty(name)) {
                ZLog.e(ZTag.TAG_DEBUG, "未获取到应用名(%s), 使用默认(%s)", packageName, defaultName);
                name = defaultName;
            }
            MemoryCacheManager.saveCache(Key.getAppNameKey(packageName), name);
        }
//        XHLog.i(LcTag.TAG_DEBUG, "获取到应用名(%s) : (%s)", packageName, name);
        return name;
    }

    public static int getAppLatelyVersionCode(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return 1;
        }
        AppShortcut appShortcut = getAppShortcut(packageName);
        int versionCode;
        if (appShortcut != null) {
            versionCode = appShortcut.getVersionCode();
        } else {
            versionCode = ZStringUtil.parseInt(latelyVersionFile.getProperty(Key.getAppVersionKey(packageName)));
        }
        if (versionCode <= 0) {
            versionCode = 1;
        }
        return versionCode;
    }
    // --------------------------------------------------

    // --------------------------------------------------
}
