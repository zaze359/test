package com.zaze.demo.debug;

import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zaze.demo.app.MyApplication;
import com.zaze.utils.AppUtil;
import com.zaze.utils.FileUtil;
import com.zaze.utils.JsonUtil;
import com.zaze.utils.cache.MemoryCacheManager;
import com.zaze.utils.config.ZConfigHelper;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:22
 */
public class ApplicationManager {
    private static final Object lockObj = new Object();
    private static ZConfigHelper latelyUidFile = ZConfigHelper.newInstance(FileUtil.getSDCardRoot() + "/xuehai/keep/stats/LatelyUid.stats");

    public static List<AppShortcut> getInstalledAppShortcuts() {
        synchronized (lockObj) {
            String key = "getInstalledAppShortcuts";
            List<AppShortcut> appShortcutList = JsonUtil.parseJsonToList(MemoryCacheManager.getCache(key), new TypeToken<List<AppShortcut>>() {
            }.getType());
            if (appShortcutList == null) {
                List<ApplicationInfo> list = AppUtil.getInstalledApplications(MyApplication.getInstance(), 0);
                appShortcutList = new ArrayList<>();
                for (ApplicationInfo applicationInfo : list) {
                    AppShortcut appShortcut = AppShortcut.transform(applicationInfo,
                            AppUtil.getAppName(MyApplication.getInstance(), applicationInfo.packageName, ""));
                    String value = JsonUtil.objToJson(appShortcut);
                    MemoryCacheManager.saveCache(applicationInfo.packageName, value);
                    MemoryCacheManager.saveCache(String.valueOf(applicationInfo.uid), value);
                    latelyUidFile.setProperty(String.valueOf(applicationInfo.uid), value);
                    ZLog.i(ZTag.TAG_DEBUG, applicationInfo.uid + " : " + applicationInfo.packageName);
                    appShortcutList.add(appShortcut);
                }
                MemoryCacheManager.saveCache(key, JsonUtil.objToJson(appShortcutList));
            }
            return appShortcutList;
        }
    }

    public static AppShortcut getAppShortcutByUid(int uid) {
        synchronized (lockObj) {
            AppShortcut appShortcut = JsonUtil.parseJson(MemoryCacheManager.getCache(String.valueOf(uid)), AppShortcut.class);
            if (appShortcut == null) {
                String packageName = AppUtil.getNameForUid(MyApplication.getInstance(), uid);
                if (!TextUtils.isEmpty(packageName)) {
                    appShortcut = AppShortcut.transform(AppUtil.getApplicationInfo(MyApplication.getInstance(), packageName),
                            AppUtil.getAppName(MyApplication.getInstance(), packageName, ""));
                }
                if (appShortcut == null) {
                    appShortcut = JsonUtil.parseJson(latelyUidFile.getProperty(String.valueOf(uid)), AppShortcut.class);
                    if (appShortcut == null) {
                        appShortcut = new AppShortcut();
                        appShortcut.setPackageName(packageName);
                        appShortcut.setName(packageName);
                    }
                    MemoryCacheManager.deleteCache(String.valueOf(uid));
                } else {
                    String json = JsonUtil.objToJson(appShortcut);
                    latelyUidFile.setProperty(String.valueOf(uid), json);
                    MemoryCacheManager.saveCache(String.valueOf(uid), json);
                }
            }
            ZLog.i(ZTag.TAG_DEBUG, "%s(%s)", appShortcut, uid);
            return appShortcut;
        }
    }
}
