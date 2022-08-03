package com.zaze.demo.viewmodels

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.DemoFragment
import com.zaze.demo.debug.LogDirListener
import com.zaze.demo.debug.kotlin.KData
import com.zaze.demo.debug.test.*
import com.zaze.demo.usagestats.AppUsageTest
import com.zaze.utils.DeviceUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.cmd.CommandBox
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch
import java.lang.StringBuilder

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-14 - 16:23
 */
class MainViewModel : AbsViewModel() {
    val fragmentListData = MutableLiveData<ArrayList<AbsFragment>>()
    private val dirListener = LogDirListener(FileUtil.getSDCardRoot() + "/")

    fun init() {
        dirListener.startWatching()
    }

    fun loadFragments() {
        viewModelScope.launch {
            val fragmentList = ArrayList<AbsFragment>()
            fragmentList.add(DemoFragment.newInstance("0"))
            fragmentList.add(DemoFragment.newInstance("1"))
            fragmentListData.value = fragmentList
        }
    }

    private var appUsageTest: AppUsageTest? = null

    fun test(activity: Activity) {
//        if (appUsageTest == null) {
//            appUsageTest = AppUsageTest(activity).apply {
//                run()
//            }
//        }


        listOf(
            TestByJava(),
//            TestBattery(),
//            TestFile(),
//            TestUserHandle(),
//            TestCommand(),
        ).forEach {
            it.doTest(activity)
        }

        //            val msg = Message.obtain()
//            msg.replyTo = messenger
//            sendMessenger?.send(msg)
//
        // --------------------------------------------------
        Snackbar.make(activity.window.decorView, "LENGTH_INDEFINITE", Snackbar.LENGTH_INDEFINITE)
            .setTextColor(Color.WHITE)
            .setAction("action") { }
            .setActionTextColor(Color.RED).show()
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_LONG)
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_SHORT)

        // --------------------------------------------------
        // --------------------------------------------------

//            val wakeLockTask = PowerLockTask(this@MainActivity)
//            Thread(wakeLockTask).start()
//            Thread(AlarmTask(this)).start()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
//                AppUtil.getInstalledApplications(this).forEach {
//                    ZLog.i(ZTag.TAG, "${it.packageName} isIgnoringBatteryOptimizations: ${pm.isIgnoringBatteryOptimizations(it.packageName)}")
//                }
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
//                    it.data = Uri.parse("package:" + this.getPackageName())
//                })
//            }
    }


    fun getDefaultLauncher(context: Context) {
        val activities = ArrayList<ComponentName>()
        context.packageManager.getPreferredActivities(arrayListOf(IntentFilter(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }), activities, null)
        for (componentName in activities) {
            Log.i("getPreferredActivities", "componentName : $componentName")
        }
        val defaultLauncher =
            context.packageManager.resolveActivity(Intent(Intent.ACTION_MAIN).apply {
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


    override fun onCleared() {
        super.onCleared()
        dirListener.stopWatching()
    }

}