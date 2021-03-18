package com.zaze.demo.debug.kotlin

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.UserHandle
import android.os.UserManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.zaze.utils.log.ZTag
import net.contrapunctus.lzma.Version


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
object KotlinDebug {

    var thread = Thread()
    fun test(context: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            Log.i("battery", "percent : ${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)}%")
        } else {
            val batteryInfoIntent: Intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryInfoIntent.getIntExtra("level", 0)
            val batterySum = batteryInfoIntent.getIntExtra("scale", 100)
            val percentBattery = 100 * level / batterySum
            Log.i("battery", "level = $level")
            Log.i("battery", "batterySum = $batterySum")
            Log.i("battery", "percent is $percentBattery%")
        }
        Log.i("battery", "currentTimeMillis: ${System.currentTimeMillis()}")

//        val set = HashSet<Int>()
//        for (i in 0..100) {
//            val uid = getUserHandleForUid(context, i)
//            if (uid >= 0) {
//                set.add(uid)
//            }
//            Log.e("aaaa", "getUserHandleForUid : $uid")
//        }
    }

    private fun getUserHandleForUid(context: Context, uid: Int): Int {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return UserHandle.getUserHandleForUid(uid)?.hashCode() ?: -1
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return UserManager::class.java.getMethod(
                            "getUserSerialNumber",
                            Int::class.javaPrimitiveType
                    ).invoke(getUserManager(context), uid) as Int
                }
            }
        } catch (e: Throwable) {
            Log.e(ZTag.TAG_ERROR, "getUserHandleForUid", e)
        }
        return -1
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getUserManager(context: Context): UserManager? {
        return context.getSystemService(Context.USER_SERVICE) as UserManager?
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
    }

}