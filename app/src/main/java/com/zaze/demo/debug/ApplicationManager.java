package com.zaze.demo.debug;

import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zaze.demo.app.MyApplication;
import com.zaze.utils.ZAppUtil;
import com.zaze.utils.ZFileUtil;
import com.zaze.utils.ZJsonUtil;
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
    private static ZConfigHelper latelyUidFile = ZConfigHelper.newInstance(ZFileUtil.INSTANCE.getSDCardRoot() + "/xuehai/keep/stats/LatelyUid.stats");

    public static List<AppShortcut> getInstalledAppShortcuts() {
        synchronized (lockObj) {
            String key = "getInstalledAppShortcuts";
            List<AppShortcut> appShortcutList = ZJsonUtil.parseJsonToList(MemoryCacheManager.getCache(key), new TypeToken<List<AppShortcut>>() {
            }.getType());
            if (appShortcutList == null) {
                List<ApplicationInfo> list = ZAppUtil.INSTANCE.getInstalledApplications(MyApplication.getInstance(), 0);
                appShortcutList = new ArrayList<>();
                for (ApplicationInfo applicationInfo : list) {
                    AppShortcut appShortcut = AppShortcut.transform(applicationInfo,
                            ZAppUtil.INSTANCE.getAppName(MyApplication.getInstance(), applicationInfo.packageName, ""));
                    String value = ZJsonUtil.objToJson(appShortcut);
                    MemoryCacheManager.saveCache(applicationInfo.packageName, value);
                    MemoryCacheManager.saveCache(String.valueOf(applicationInfo.uid), value);
                    latelyUidFile.setProperty(String.valueOf(applicationInfo.uid), value);
                    ZLog.i(ZTag.TAG_DEBUG, applicationInfo.uid + " : " + applicationInfo.packageName);
                    appShortcutList.add(appShortcut);
                }
                MemoryCacheManager.saveCache(key, ZJsonUtil.objToJson(appShortcutList));
            }
            return appShortcutList;
        }
    }

    public static AppShortcut getAppShortcutByUid(int uid) {
        synchronized (lockObj) {
            AppShortcut appShortcut = ZJsonUtil.parseJson(MemoryCacheManager.getCache(String.valueOf(uid)), AppShortcut.class);
            if (appShortcut == null) {
                String name = ZAppUtil.INSTANCE.getNameForUid(MyApplication.getInstance(), uid);
                if (!TextUtils.isEmpty(name)) {
                    appShortcut = AppShortcut.transform(ZAppUtil.INSTANCE.getApplicationInfo(MyApplication.getInstance(), name),
                            ZAppUtil.INSTANCE.getAppName(MyApplication.getInstance(), name, ""));
                }
                if (appShortcut == null) {
                    appShortcut = ZJsonUtil.parseJson(latelyUidFile.getProperty(String.valueOf(uid)), AppShortcut.class);
                    MemoryCacheManager.deleteCache(String.valueOf(uid));
                } else {
                    String json = ZJsonUtil.objToJson(appShortcut);
                    latelyUidFile.setProperty(String.valueOf(uid), json);
                    MemoryCacheManager.saveCache(String.valueOf(uid), json);
                }
            }
            ZLog.i(ZTag.TAG_DEBUG, "%s(%s)", appShortcut, uid);
            return appShortcut;
        }
    }
}
