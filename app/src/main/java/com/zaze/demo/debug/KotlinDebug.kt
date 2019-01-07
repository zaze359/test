package com.zaze.demo.debug

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.SystemClock
import com.zaze.utils.FileUtil
import com.zaze.utils.ZDisplayUtil
import com.zaze.utils.ZStringUtil
import com.zaze.utils.config.ConfigHelper
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
object KotlinDebug {

    fun test(activity: Activity) {
        var result = ""
//        return showLog("print", { print() })
//        showLog("createDimensByDensity", { createDimensByDensity(ZDisplayUtil.getScreenDensity()) })
//        showLog("createDimensBySW", { createDimensBySW(768, ZDisplayUtil.getScreenWidthDp().toInt()) })
        // --------------------------------------------------
        result += (System.currentTimeMillis() - SystemClock.elapsedRealtime())
        // --------------------------------------------------
        val file = File("/sdcard/test.ini")
        val config = ConfigHelper.newInstance(file)
        val map = HashMap<String, String>()
        for (i in 0..1) {
            map["${System.currentTimeMillis()}" + i] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        }
        config.setProperty(map)
        // --------------------------------------------------
//        createDeveloperToken()
        // --------------------------------------------------
//        ZLog.i(ZTag.TAG_DEBUG, "${ZDeviceUtil.getSdFreeSpace() < 5L shl 30}")
//        ZLog.i(ZTag.TAG_DEBUG, "${ZDeviceUtil.getSdFreeSpace() < 10L shl 30}")
//        ZLog.i(ZTag.TAG_DEBUG, "${ZDateUtil.getWeek(Date())}")
        // --------------------------------------------------
//        ZLog.i(ZTag.TAG_DEBUG, "currentTimeMillis : ${System.currentTimeMillis()}")
//        ZLog.i(ZTag.TAG_DEBUG, "currentThreadTimeMillis: ${SystemClock.currentThreadTimeMillis()}")
//        ZLog.i(ZTag.TAG_DEBUG, "elapsedRealtime : ${SystemClock.elapsedRealtime()}")
        // --------------------------------------------------
//        AppUtil.startApplicationSimple(MyApplication.getInstance(), "com.xuehai.response_launcher_teacher")
//        val intent = getTargetActivityIntent(activity, "com.xh.open.WakeupActivity")
//        val intent = getTargetActivityIntent(activity, "com.xh.open.agent.AgentActivity")
//        intent?.let {
//            activity.startActivity(intent)
//        }
//        getNetType(activity)
        //
//        val packageName = "com.xuehai.response_launcher_teacher"
//        val packageName = "com.xuehai.launcher"
        // --------------------------------------------------
//        val action = ""
//        if (!AppUtil.isAppRunning(activity, action)) {
//            val intent = Intent(action)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//            activity.startActivity(intent)
//        } else {
//            ZLog.i(ZTag.TAG_DEBUG, "$action 已启动")
//        }
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
//
//        if (activeInfo == null) {
//            ZLog.e(ZTag.TAG_DEBUG, "无网络连接")
//        } else {
//            if (wifiInfo != null && wifiInfo.isConnected) {
//                ZLog.i(ZTag.TAG_DEBUG, "当前连接wifi")
//            } else if (mobileInfo != null && mobileInfo.isConnected) {
//                ZLog.i(ZTag.TAG_DEBUG, "当前使用数据流量")
//            } else {
//                ZLog.e(ZTag.TAG_DEBUG, "有网但是获取不到网络状态")
//            }
//        }
    }


    @JvmStatic
    fun getTargetActivityIntent(activity: Activity, className: String): Intent? {
        val intent = Intent()
        intent.setClassName("com.xuehai.launcher", className)
        if (isActivityExist(activity, intent)) {
            return intent
        }
        intent.setClassName("com.xuehai.response_launcher_teacher", className)
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
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zazen/values-${ZDisplayUtil.getDensityDpiName()}-sw${screenSw}dp/dimens.xml", swBuilder.toString())
        FileUtil.writeToFile("${FileUtil.getSDCardRoot()}/zazen/values-${ZDisplayUtil.getDensityDpiName()}-sw${baseSw}dp/dimens.xml", baseBuilder.toString())
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

    // --------------------------------------------------
    fun searchFile(): String {
        val fileSet = FileUtil.searchFileAndDir(File("/sdcard/"), "com.xuehai.response_launcher_teacher");
        val builder = StringBuilder()
        for (file in fileSet) {
            builder.append(file.absolutePath + "\n")
        }
        return builder.toString()
    }

    // --------------------------------------------------
    fun adb() {
    }
}