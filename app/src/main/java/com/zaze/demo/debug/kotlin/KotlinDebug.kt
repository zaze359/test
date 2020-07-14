package com.zaze.demo.debug.kotlin

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import com.zaze.demo.debug.TestDebug
import com.zaze.utils.FileUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
object KotlinDebug {

    var thread = Thread()
    fun test(context: Activity) {
        thread.interrupt()
        thread = Thread {
            ZLog.i(ZTag.TAG, "execCmdForRes start")
            val bugReport = "/sdcard/bugreport.log"
            FileUtil.createFileNotExists(bugReport)
            try {
                TestDebug.DoShellCmd("bugreport > $bugReport  2>&1")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ZLog.i(ZTag.TAG, "execCmdForRes end")
        }
        thread.start()
    }

    fun getDefaultLauncher(context: Context) {
        val activities = ArrayList<ComponentName>()
        context.packageManager.getPreferredActivities(arrayListOf(IntentFilter(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }), activities, null)
        for (componentName in activities) {
            Log.i("getPreferredActivities", "componentName : $componentName")
        }
        val defaultLauncher = context.packageManager.resolveActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }, PackageManager.MATCH_DEFAULT_ONLY)

        Log.i("defaultLauncher", "defaultLauncher : ${defaultLauncher?.activityInfo?.packageName}")

    }

    fun a() {
        // 原生字符串支持
        val aStr = String().plus(""" a \n b""")
        val bStr = String().plus(""" a \n b""")
        println("aStr == bStr ${aStr == bStr}")
        println("aStr === bStr ${aStr === bStr}")


        val bird = "20.0,1,bule"
        val (weight, age, color) = bird.split(",")
        println("$weight;$age;$color")
        val kData = KData()
        val bb = kData.copy()
        val (key, value) = kData
        println("$key;$value")
        Lambda.debug()
        Vararg.debug()
        Infix.debug()
    }

}