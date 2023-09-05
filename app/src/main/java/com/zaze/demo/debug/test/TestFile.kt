package com.zaze.demo.debug.test

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.zaze.utils.permission.PermissionHelper
import com.zaze.demo.R
import com.zaze.demo.feature.applications.ApplicationManager
import com.zaze.dynamic.DynamicLoader
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.io.FileInputStream

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 15:53
 */
class TestFile : ITest {
    private val TAG = ZTag.TAG_DEBUG + ":TestFile"
    val dir = "/sdcard/zaze"

    //        val dir = context.filesDir.absolutePath
    val oldApkPath = "${dir}/bsdiff/old.apk"
    val newApkPath = "${dir}/bsdiff/new.apk"
    val patchPath = "${dir}/bsdiff/patch.apk"

    val soLibName = "libbsdiff-android.so"
    val soPath = "${dir}/so/${soLibName}"
    override fun doTest(context: Context) {

        val resources = context.resources
        log("getResourceName: ${resources.getResourceName(R.drawable.ic_app_default)}")
        log("getResourcePackageName: ${resources.getResourcePackageName(R.drawable.ic_app_default)}")
        log("getResourceTypeName: ${resources.getResourceTypeName(R.drawable.ic_app_default)}")
        log("getResourceEntryName: ${resources.getResourceEntryName(R.drawable.ic_app_default)}")

        val appShortcut = ApplicationManager.getAppShortcut("com.zaze.apps")
        log("appShortcut sharedLibraryFiles: ${appShortcut.applicationInfo?.sharedLibraryFiles?.joinToString()}")
        log("appShortcut sourceDir: ${appShortcut.applicationInfo?.sourceDir}")
        log("appShortcut publicSourceDir: ${appShortcut.applicationInfo?.publicSourceDir}")
        log("appShortcut splitSourceDirs: ${appShortcut.applicationInfo?.splitSourceDirs}")
        log("appShortcut nativeLibraryDir: ${appShortcut.applicationInfo?.nativeLibraryDir}")

        val apkPath = oldApkPath
        log("$apkPath: ${FileUtil.exists(apkPath)}")
        val apkLoader = DynamicLoader.build(context, apkPath) ?: let {
            log("load failed $apkPath")
            return
        }
        val dynamicApk = apkLoader.dynamicApk
        val apkContext = apkLoader.context
        val packageName = dynamicApk.applicationInfo.packageName

        log("load packageName: $packageName")
        log("load sourceDir: ${dynamicApk.applicationInfo.sourceDir}")
        val apkResources = apkContext.resources
//        val apkResources = HookResources.build(context, apkPath)
        try {
            val id =
                apkResources?.getIdentifier("app_detail", "string", packageName)
            log("app_detail($id): ${apkResources?.getString(id ?: 0x0)}")

            val intent = Intent()
//            intent.setAction("android.intent.action.MAIN")
            intent.setPackage(packageName)
            intent.setExtrasClassLoader(apkContext.classLoader)
//            intent.setClassName(packageName, "MainActivity")
            val list = apkContext.packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
            if (list.isEmpty()) ZLog.e(ZTag.TAG_DEBUG, "指定页面不存在($intent)")
            list.forEach {
                log("queryIntentActivities: ${it.activityInfo.name}")
            }
            log("loadClass MainActivity: ${apkContext.classLoader.loadClass("$packageName.MainActivity")}")
//            apkLoader.apkContext.startActivity()

            apkContext.packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)?.activities?.forEach {
                log("activities: ${it.name}")
            } ?: log("not found")

        } catch (e: Throwable) {
            e.printStackTrace()
        }

        val fd = FileInputStream(File(oldApkPath)).fd

//        try {
//            System.load(copyLibToData(context, soPath, soLibName))
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }



//        val content = "aaaa"
//        FileUtil.writeToFile("/sdcard/Android/data/com.baidu.map.location/aaa.txt", content)
//        FileUtil.writeToFile(
//            "/sdcard/Android/data/${context.packageName}/aaa.txt",
//            content
//        )
//        FileUtil.writeToFile("/sdcard/Android/media/com.baidu.map.location/aaa.txt", content)
//        FileUtil.writeToFile(
//            "/sdcard/Android/media/${context.packageName}/aaa.txt",
//            content
//        )
//        FileUtil.writeToFile(
//            "${
//                context
//                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
//            }/aaa.txt", content
//        )
//        FileUtil.writeToFile(
//            "${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}/aaa.txt",
//            content
//        )
//
//        context.fileList()?.forEach {
//            Log.i("fileList", "file : $it")
//        }
//        Log.i("fileList", "file : ${context.filesDir.absolutePath}")
//
//        ContextCompat.getExternalCacheDirs(context).forEach {
//            Log.i(
//                "fileList",
//                "getExternalCacheDirs : ${it.absolutePath}"
//            )
//        }
//        ContextCompat.getExternalFilesDirs(context, null)[0].listFiles()?.forEach {
//            Log.i(
//                "fileList",
//                "getExternalFilesDirs : ${it.absolutePath}"
//            )
//        }
//        Log.i(
//            "fileList",
//            "getExternalFilesDirs : ${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}"
//        )
//
//        listOf(
//            "Environment.getRootDirectory():${Environment.getRootDirectory().absolutePath}",
//            "Environment.getDataDirectory():${Environment.getDataDirectory().absolutePath}",
//            "Environment.getDownloadCacheDirectory():${Environment.getDownloadCacheDirectory().absolutePath}",
//            "Environment.getExternalFilesDir():${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}",
//        ).forEach {
//            ZLog.i(ZTag.TAG, "111 $it")
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            context.getExternalFilesDirs(null)?.forEach {
//                ZLog.i(ZTag.TAG, "222 ${it.absolutePath}")
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            listOf(
//                "Environment.getStorageDirectory():${Environment.getStorageDirectory().absolutePath}",
//            ).forEach {
//                ZLog.i(ZTag.TAG, "333 $it")
//            }
//        }
    }

    // 拷贝到 data 下加载
    private fun copyLibToData(context: Context, sourcePath: String, libName: String): String {
        val soDir = File(context.filesDir, "so")
        val soFile = File(soDir.absolutePath, libName)
        if (soFile.exists()) {
            soFile.delete()
        }
        // copy
        FileUtil.copy(File(sourcePath), soFile)
        return soFile.absolutePath
    }



    private fun printPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            log("isExternalStorageManager : ${Environment.isExternalStorageManager()}")
        }
        log(
            "MANAGE_EXTERNAL_STORAGE : ${
                PermissionHelper.hasPermissions(
                    context,
                    arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                )
            }"
        )
        log(
            "WRITE_EXTERNAL_STORAGE : ${
                PermissionHelper.hasPermissions(
                    context,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            }"
        )
        log(
            "READ_EXTERNAL_STORAGE : ${
                PermissionHelper.hasPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            }"
        )
    }

    private fun log(message: String) {
        ZLog.i(TAG, message)
    }

}