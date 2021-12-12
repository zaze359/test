package com.zaze.demo.debug.test

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 15:53
 */
class TestFile : ITest {

    override fun doTest(context: Context) {
        val content = "aaaa"
        FileUtil.writeToFile("/sdcard/Android/data/com.baidu.map.location/aaa.txt", content)
        FileUtil.writeToFile(
            "/sdcard/Android/data/${context.packageName}/aaa.txt",
            content
        )
        FileUtil.writeToFile("/sdcard/Android/media/com.baidu.map.location/aaa.txt", content)
        FileUtil.writeToFile(
            "/sdcard/Android/media/${context.packageName}/aaa.txt",
            content
        )
        FileUtil.writeToFile(
            "${
                context
                    .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            }/aaa.txt", content
        )
        FileUtil.writeToFile(
            "${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}/aaa.txt",
            content
        )

        context.fileList()?.forEach {
            Log.i("fileList", "file : $it")
        }
        Log.i("fileList", "file : ${context.filesDir.absolutePath}")

        ContextCompat.getExternalCacheDirs(context).forEach {
            Log.i(
                "fileList",
                "getExternalCacheDirs : ${it.absolutePath}"
            )
        }
        ContextCompat.getExternalFilesDirs(context, null)[0].listFiles()?.forEach {
            Log.i(
                "fileList",
                "getExternalFilesDirs : ${it.absolutePath}"
            )
        }
        Log.i(
            "fileList",
            "getExternalFilesDirs : ${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}"
        )

        listOf(
            "Environment.getRootDirectory():${Environment.getRootDirectory().absolutePath}",
            "Environment.getDataDirectory():${Environment.getDataDirectory().absolutePath}",
            "Environment.getDownloadCacheDirectory():${Environment.getDownloadCacheDirectory().absolutePath}",
            "Environment.getExternalFilesDir():${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}",
        ).forEach {
            ZLog.i(ZTag.TAG, "111 $it")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getExternalFilesDirs(null)?.forEach {
                ZLog.i(ZTag.TAG, "222 ${it.absolutePath}")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            listOf(
                "Environment.getStorageDirectory():${Environment.getStorageDirectory().absolutePath}",
            ).forEach {
                ZLog.i(ZTag.TAG, "333 $it")
            }
        }


    }

}