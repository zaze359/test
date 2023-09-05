package com.zaze.dynamic

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Resources
import com.zaze.utils.AppUtil
import dalvik.system.DexClassLoader
import java.io.File


class DynamicApk(
    hostAppContext: Context,
    val packageInfo: PackageInfo,
    val applicationInfo: ApplicationInfo,
    val resources: Resources,
    val classLoader: ClassLoader
) {
    companion object {
        fun createOrNull(hostAppContext: Context, apkAbsPath: String): DynamicApk? {
            if (!File(apkAbsPath).exists()) {
                return null
            }
            val packageInfo =
                AppUtil.getPackageArchiveInfo(hostAppContext.applicationContext, apkAbsPath)
                    ?: return null
            //
            val applicationInfo = packageInfo.applicationInfo ?: ApplicationInfo().also {
                it.packageName = packageInfo.packageName
            }
            // 创建 resource
            // 需要先添加资源路径，才能正常的获取到资源
            applicationInfo.sourceDir = apkAbsPath
            applicationInfo.publicSourceDir = apkAbsPath
            val resources = hostAppContext.packageManager.getResourcesForApplication(applicationInfo)
            // 创建 classLoader
            val apk = File(apkAbsPath)
            val odexDir = File(apk.parent, apk.name + "_odex")
            val libDir = File(apk.parent, apk.name + "_lib")
            val classLoader = DexClassLoader(
                apkAbsPath,
                odexDir.absolutePath,
                libDir.absolutePath,
                hostAppContext.classLoader
            )
            return DynamicApk(hostAppContext, packageInfo, applicationInfo, resources, classLoader)
        }

//        private fun createResource(hostAppContext: Context, apkAbsPath: String): Resources? {
//            val packageInfo =
//                AppUtil.getPackageArchiveInfo(hostAppContext.applicationContext, apkAbsPath)
//                    ?: return null
//            val applicationInfo = packageInfo.applicationInfo ?: ApplicationInfo().also {
//                it.packageName = packageInfo.packageName
//            }
//            // 创建 resources
//            // 需要先添加资源路径，才能正常的获取到资源
//            applicationInfo.sourceDir = apkAbsPath
//            applicationInfo.publicSourceDir = apkAbsPath
//            return hostAppContext.packageManager.getResourcesForApplication(applicationInfo)
//        }
    }
}