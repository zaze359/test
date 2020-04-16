package com.zaze.demo.debug.kotlin

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaze.utils.DisplayUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.ZStringUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
object KotlinDebug {

    fun test(context: Activity) {
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