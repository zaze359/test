package com.zaze.demo.debug

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
import com.zaze.utils.AppUtil
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
object Temp {

    fun test(context: Activity) {
//        val a: Int? = 1
//        a?.let {
//            if (it == 1) {
//                true
//            } else if (it == 2) {
//                true
//            } else {
//                false
//            }.let { state ->
//                Log.w("Debug", "a1111 $state")
//            }
//        }
//
//        a?.let {
//            if (it == 1) {
//                true
//            } else {
//                false
//            }.let { state ->
//                Log.w("Debug", "a2222 $state")
//            }
//        }


        var result = ""
//        if (hasPermissionToReadNetworkStats(activity)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val networkStatsManager = activity.getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager
//                // 获取到目前为止设备的Wi-Fi流量统计
//                val bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis())
//                Log.i("Info", "Total: " + (bucket.rxBytes + bucket.txBytes))
//            }
//        }
        //
//        WifiCompat.listenerByConn()
//        WifiCompat.listenerByJob(MyApplication.getInstance())
        // --------------------------------------------------
//        ThreadPlugins.runInWorkThread(Runnable {
//            DeviceChecker(activity).checkSafely()
//        }, 0)

//        arrayListOf<String>("1", "2", "3").forEach {
//            if (it == "1") {
//                Log.i("test", "itititit : $it")
//                return@forEach
//            }
//            Log.i("test", it)
//        }

//        Log.i("test", "getSignatures = " + SignaturesUtil.getSignatures(context, "MD5")!!)
//        AppUtil.getActivityManager(context).runningAppProcesses.forEach {
//            Log.i("test", "runningAppProcesses = ${it.processName}")
//        }
        AppUtil.getActivityManager(context).getRunningTasks(3).forEach {
            Log.i("test", "runningAppProcesses = ${it.topActivity?.packageName}")
        }

    }


    fun getMoney() {
        var RMB = 30273
        val interestRate = 0.0242 / 365
        val splitCount = 12
        val totalTime = splitCount * 30
        var gotMoney = 0.0
        for (i in 0..totalTime) {
            if (i % 30 == 0) {
                RMB -= RMB / splitCount
            }
            if (RMB <= 0) {
                break
            }
            gotMoney += RMB * interestRate
        }
        Log.i("gotMoney", "gotMoney: $gotMoney")
    }

    fun getDefaultInputMethod(context: Context): String {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val list = imm.inputMethodList
            if (list != null && !list.isEmpty()) {
                return list[0].id
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun hasPermissionToReadNetworkStats(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
        } else {
        }

        val appOps = activity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), activity.packageName)
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true
        }
        // 打开“有权查看使用情况的应用”页面
        activity.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        return false
    }

    @JvmStatic
    fun getNetType(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val activeInfo = connectivityManager.activeNetworkInfo
        //        XHLog.i(LcTag.TAG_DEBUG, "mobileInfo : $mobileInfo")
//        XHLog.i(LcTag.TAG_DEBUG, "wifiInfo : $wifiInfo")

        ZLog.i(ZTag.TAG_DEBUG, "activeInfo : $activeInfo")
        if (wifiInfo != null) {
            if (wifiInfo.isConnected) {
                ZLog.i(ZTag.TAG_DEBUG, "当前连接wifi isConnected")
                return
            } else {
                ZLog.e(ZTag.TAG_DEBUG, "当前连接wifi, 不可用")
            }
        }
        if (mobileInfo != null) {
            if (mobileInfo.isConnected) {
                ZLog.i(ZTag.TAG_DEBUG, "当前使用数据流量, isConnected")
                return
            } else {
                ZLog.e(ZTag.TAG_DEBUG, "当前使用数据流量, 不可用")
            }
        }
        ZLog.e(ZTag.TAG_DEBUG, "获取不到网络状态")
    }


    @JvmStatic
    fun getTargetActivityIntent(activity: Activity, className: String): Intent? {
        val intent = Intent()
        intent.setClassName("com.aaa", className)
        if (isActivityExist(activity, intent)) {
            return intent
        }
        intent.setClassName("com.aaaa", className)
        if (isActivityExist(activity, intent)) {
            return intent
        }
        ZLog.e(ZTag.TAG_DEBUG, "指定页面不存在($className)")
        return null
    }

    @JvmStatic
    fun isActivityExist(context: Context, intent: Intent?): Boolean {
        return if (intent != null) {
            !context.packageManager.queryIntentActivities(intent, 0).isEmpty()
        } else {
            false
        }
    }


    private fun createDimensByDensity(screenDensity: Float): String {
        var dp = 1
        val baseBuilder = initBuilder(StringBuilder())
        val dpBuilder = initBuilder(StringBuilder())
        do {
            if (dp <= 10 || dp % 2 == 0) {
                dpBuilder.append(ZStringUtil.format("\t<dimen name=\"dp_$dp\">%1.1fdp</dimen>\n", dp * 1f / screenDensity))
                baseBuilder.append("\t<dimen name=\"dp_$dp\">${dp}dp</dimen>\n")
            }
            dp++
        } while (dp <= 2000)

        dpBuilder.append("</resources>")
        baseBuilder.append("</resources>")
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zaze/z_dimens/dimens.xml", dpBuilder.toString())
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zaze/z_dimens/dimens_base.xml", baseBuilder.toString())
        return dpBuilder.toString()
    }


    private fun createDimensBySW(baseSw: Int, screenSw: Int): String {
        var dp = 1
        val baseBuilder = initBuilder(StringBuilder())
        val swBuilder = initBuilder(StringBuilder())
        do {
            if (dp <= 10 || dp % 2 == 0) {
                baseBuilder.append("\t<dimen name=\"dp_$dp\">${dp}dp</dimen>\n")
                swBuilder.append(ZStringUtil.format("\t<dimen name=\"dp_$dp\">%1.1fdp</dimen>\n", 1.0f * dp * screenSw / baseSw))
            }
            dp++
        } while (dp <= 1024)
        var sp = 1
        do {
            if (sp <= 10 || sp % 2 == 0) {
                baseBuilder.append("\t<dimen name=\"sp_$sp\">${sp}sp</dimen>\n")
                swBuilder.append(ZStringUtil.format("\t<dimen name=\"sp_$sp\">%1.1fsp</dimen>\n", 1.0f * sp * screenSw / baseSw))
            }
            sp++
        } while (sp <= 100)
        swBuilder.append("</resources>")
        baseBuilder.append("</resources>")
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zazen/values-${DisplayUtil.getDensityDpiName()}-sw${screenSw}dp/dimens.xml", swBuilder.toString())
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zazen/values-${DisplayUtil.getDensityDpiName()}-sw${baseSw}dp/dimens.xml", baseBuilder.toString())
        return swBuilder.toString()
    }

    private fun initBuilder(builder: StringBuilder): StringBuilder {
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        builder.append("<resources>\n")
        return builder
    }


    // --------------------------------------------------

    fun showLog(msg: String, body: () -> String): String {
        val print = body()
        ZLog.i(msg, print)
        return print
    }


    // --------------------------------------------------

    fun clearCacheData(): String {
//        AppUtil.clearDataInfo(MyApplication.getInstance(), MyApplication.getInstance().packageName)
//        return "" + FileUtil.deleteFile("/data/data/" + MyApplication.getInstance().packageName)
        return ""
    }
}