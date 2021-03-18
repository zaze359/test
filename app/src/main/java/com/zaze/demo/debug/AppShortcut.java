package com.zaze.demo.debug;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import androidx.annotation.NonNull;

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
    private boolean isInstalled = false;
    private boolean copyEnable = true;
    private long firstInstallTime = 0;
    private long lastUpdateTime = 0;

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

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public String getSigningInfo() {
        return signingInfo;
    }

    public void setSigningInfo(String signingInfo) {
        this.signingInfo = signingInfo;
    }

    public boolean isCopyEnable() {
        return copyEnable;
    }

    public void setCopyEnable(boolean copyEnable) {
        this.copyEnable = copyEnable;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public static @NonNull
    AppShortcut transform(Context context, final @NonNull PackageInfo packageInfo) {
        AppShortcut appShortcut = new AppShortcut();
        appShortcut.setPackageName(packageInfo.packageName);
        appShortcut.setVersionCode(packageInfo.versionCode);
        appShortcut.setVersionName(packageInfo.versionName);
        appShortcut.setSigningInfo(SignaturesUtil.getSignatures(new ContextWrapper(context) {
            @Override
            public String getPackageName() {
                return packageInfo.packageName;
            }
        }, "MD5"));
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        if (applicationInfo != null) {
            appShortcut.setName(context.getPackageManager().getApplicationLabel(applicationInfo).toString());
            appShortcut.setInstalled(true);
            appShortcut.setSourceDir(applicationInfo.sourceDir);
            appShortcut.setFlags(applicationInfo.flags);
            appShortcut.setUid(applicationInfo.uid);
        } else {
            appShortcut.setInstalled(false);
        }
        appShortcut.setFirstInstallTime(packageInfo.firstInstallTime);
        appShortcut.setLastUpdateTime(packageInfo.lastUpdateTime);
        return appShortcut;
    }

    @NotNull
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
                ", signingInfo='" + signingInfo + '\'' +
                ", isInstalled=" + isInstalled +
                ", copyEnable=" + copyEnable +
                ", firstInstallTime=" + firstInstallTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
