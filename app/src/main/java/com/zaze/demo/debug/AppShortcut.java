package com.zaze.demo.debug;

import android.content.pm.ApplicationInfo;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:26
 */
public class AppShortcut {
    private String name;
    private String packageName;
    private int uid;
    private int flags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public String toString() {
        return "AppShortcut{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", uid=" + uid +
                '}';
    }

    public static AppShortcut transform(ApplicationInfo applicationInfo, String appName) {
        if (applicationInfo != null) {
            AppShortcut appShortcut = new AppShortcut();
            appShortcut.setName(appName);
            appShortcut.setPackageName(applicationInfo.packageName);
            appShortcut.setUid(applicationInfo.uid);
            appShortcut.setFlags(applicationInfo.flags);
            return appShortcut;
        } else {
            return null;
        }
    }


}
