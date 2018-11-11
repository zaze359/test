package com.zaze.demo.debug;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.zaze.utils.AppUtil;
import com.zaze.utils.SignaturesUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Description : 应用快捷信息(汇总最新的 Applicaiton 和 packageInfo)
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 17:26
 */
public class AppShortcut {
    private String name;
    private String packageName;
    private int versionCode;
    private String versionName;
    private String sourceDir;
    private int uid;
    private int flags;
    private String signingInfo;

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

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
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

    public String getSigningInfo() {
        return signingInfo;
    }

    public void setSigningInfo(String signingInfo) {
        this.signingInfo = signingInfo;
    }

    public static AppShortcut transform(Context context, @NotNull ApplicationInfo applicationInfo, @NotNull PackageInfo packageInfo) {
        AppShortcut appShortcut = new AppShortcut();
        appShortcut.setVersionCode(packageInfo.versionCode);
        appShortcut.setVersionName(packageInfo.versionName);
        appShortcut.setPackageName(applicationInfo.packageName);
        appShortcut.setName(AppUtil.getAppName(context.getApplicationContext(), applicationInfo.packageName, ""));
        appShortcut.setSourceDir(applicationInfo.sourceDir);
        appShortcut.setFlags(applicationInfo.flags);
        appShortcut.setUid(applicationInfo.uid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            appShortcut.setSigningInfo(packageInfo.signingInfo.toString());
        } else {
            appShortcut.setSigningInfo(SignaturesUtil.getMd5(packageInfo.signatures));
        }
        return appShortcut;
    }

    @Override
    public String toString() {
        return "AppShortcut{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", sourceDir='" + sourceDir + '\'' +
                ", uid=" + uid +
                ", flags=" + flags +
                '}';
    }
}
