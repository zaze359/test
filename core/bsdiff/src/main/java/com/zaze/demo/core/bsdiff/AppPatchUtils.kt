package com.zaze.demo.core.bsdiff

object AppPatchUtils {
    init {
        System.loadLibrary("bsdiff-android")
    }

    /**
     * 差量计算补丁
     * [oldApkPath] 旧版本 apk 路径
     * [newApkPath] 新版本 apk 路径
     * [patchPath] 生成的补丁包路径
     */
    external fun diff(oldApkPath: String, newApkPath: String, patchPath: String): Int

    /**
     * 应用补丁包
     * [oldApkPath] 旧版本 apk 路径
     * [newApkPath] 应用补丁后的输出apk路径
     * [patchPath] 补丁包路径
     */
    external fun applyPatch(oldApkPath: String, newApkPath: String, patchPath: String): Int

}