package com.zaze.demo.feature.applications;

import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.zaze.common.base.BaseApplication;
import com.zaze.utils.AppUtil;
import com.zaze.utils.FileUtil;
import com.zaze.utils.JsonUtil;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.config.ConfigHelper;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.NotNull;

/**
 * Description : 对应用信息进行管理
 * 当应用发生变化时清空缓存, 否则有些从内存缓存中读取;
 * 仅当获取不到应用信息时尝试从文件缓存中读取(用于特殊场景).
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:22
 */
@Deprecated
public class ApplicationManager2 {

    /**
     * 记录最近的一个 uid, 仅用于获取uid时使用
     */
    private static ConfigHelper latelyUidFile = ConfigHelper.newInstance(FileUtil.getSDCardRoot() + "/zaze/LatelyUid.stats");

    /**
     * 记录最近一个versionCode
     */
    private static ConfigHelper latelyVersionFile = ConfigHelper.newInstance(FileUtil.getSDCardRoot() + "/zaze/LatelyVersion.stats");


    private static class Key {
        private static String getAppUidKey(int uid) {
            return "am_uid_" + uid;
        }

        private static String getAppVersionKey(String packageName) {
            return "am_version_" + packageName;
        }

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
            ZLog.i(ZTag.TAG_DEBUG, "getAppShortcutByUid(" + uid + "): " + packageName);
            if (!TextUtils.isEmpty(packageName)) {
                packageName = packageName.split(":")[0];
            }
        }
        if (!TextUtils.isEmpty(packageName)) {
            appShortcut = ApplicationManager.INSTANCE.getAppShortcut(BaseApplication.getInstance(), packageName);
            if (!appShortcut.isInstalled()) {
                appShortcut = JsonUtil.parseJson(latelyUidFile.getProperty(key), AppShortcut.class);
            } else {
                latelyUidFile.setProperty(key, JsonUtil.objToJson(appShortcut));
                MemoryCacheManager.saveCache(key, packageName);
            }
        }
        return appShortcut;
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
            appShortcut = AppShortcut.create(BaseApplication.getInstance(), packageInfo);
        }
        if (appShortcut != null) {
//            ApplicationManager.INSTANCE.saveShortcutToCache(packageName, appShortcut);
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
        return AppShortcut.Companion.empty(packageName);
    }

    public static int getAppLatelyVersionCode(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return 1;
        }
        AppShortcut appShortcut = ApplicationManager.INSTANCE.getAppShortcut(BaseApplication.getInstance(), packageName);
        int versionCode;
        if (appShortcut != null) {
            versionCode = (int)appShortcut.getVersionCode();
        } else {
            versionCode = ZStringUtil.parseInt(latelyVersionFile.getProperty(Key.getAppVersionKey(packageName)));
        }
        if (versionCode <= 0) {
            versionCode = 1;
        }
        return versionCode;
    }
}
