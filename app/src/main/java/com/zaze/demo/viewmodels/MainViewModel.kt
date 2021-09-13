package com.zaze.demo.viewmodels

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.BaseApplication
import com.zaze.demo.DemoFragment
import com.zaze.demo.debug.LogDirListener
import com.zaze.demo.debug.TestDebug
import com.zaze.demo.debug.kotlin.KotlinDebug
import com.zaze.utils.DeviceUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun test(activity: Activity) {
        //            val msg = Message.obtain()
//            msg.replyTo = messenger
//            sendMessenger?.send(msg)
//

        val content = "aaaa"
        FileUtil.writeToFile("/sdcard/Android/data/com.baidu.map.location/aaa.txt", content)
        FileUtil.writeToFile("/sdcard/Android/data/${BaseApplication.getInstance().packageName}/aaa.txt", content)
        FileUtil.writeToFile("/sdcard/Android/media/com.baidu.map.location/aaa.txt", content)
        FileUtil.writeToFile("/sdcard/Android/media/${BaseApplication.getInstance().packageName}/aaa.txt", content)
        FileUtil.writeToFile("${BaseApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}/aaa.txt", content)
        FileUtil.writeToFile("${BaseApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}/aaa.txt", content)


        Snackbar.make(activity.window.decorView, "LENGTH_INDEFINITE", Snackbar.LENGTH_INDEFINITE)
            .setTextColor(Color.WHITE)
            .setAction("action") { }
            .setActionTextColor(Color.RED).show()
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_LONG)
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_SHORT)
        KotlinDebug.test(activity)
        TestDebug.test(activity)
        System.getProperties().forEach {
            ZLog.i(ZTag.TAG, "System.getProperties: ${it.key}=${it.value}")
        }
        val builder = StringBuilder()
        ZCommand.execCmdForRes(arrayOf("getprop")).successList.forEach {
            builder.append(it + "\n")
        }
        ZLog.i(ZTag.TAG, "getDeviceId: ${DeviceUtil.getDeviceId(activity)}")
        ZLog.i(ZTag.TAG, "getUUID: ${DeviceUtil.getUUID(activity)}")
        ZLog.i(
            ZTag.TAG,
            "serialno: ${ZCommand.execCmdForRes("getprop ro.serialno").successMsg}"
        )
//
//            builder.toString()
//                    .replace("[", "")
//                    .replace("]", "")
//                    .split("\n").forEach {
//                        val kv = it.split(": ")
//                        if (kv.size >= 2) {
//                            repeat(kv.size) {
//                                ZLog.i(ZTag.TAG, "getProperties: : ${kv[0]}: ${kv[1]}")
//                            }
//                        }
//                    }

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

    override fun onCleared() {
        super.onCleared()
        dirListener.stopWatching()
    }

}