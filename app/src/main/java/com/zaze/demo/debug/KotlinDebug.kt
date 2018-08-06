package com.zaze.demo.debug

import android.os.SystemClock
import com.zaze.utils.FileUtil
import com.zaze.utils.ZDeviceUtil
import com.zaze.utils.ZDisplayUtil
import com.zaze.utils.ZStringUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
object KotlinDebug {

    fun test() {
        var result = ""
//        return showLog("print", { print() })
//        showLog("createDimensByDensity", { createDimensByDensity(ZDisplayUtil.getScreenDensity()) })
//        showLog("createDimensBySW", { createDimensBySW(768, ZDisplayUtil.getScreenWidthDp().toInt()) })
        // --------------------------------------------------
        result += (System.currentTimeMillis() - SystemClock.elapsedRealtime())
        // --------------------------------------------------
//        val file = File("/sdcard/test.ini")
//        val config = ZConfigHelper.newInstance(file)
//        val map = HashMap<String, String>()
//        for (i in 0..100000) {
//            map.put("${System.currentTimeMillis()}" + i, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//        }
//        config.setProperty(map)
        // --------------------------------------------------
//        createDeveloperToken()
        // --------------------------------------------------
        ZLog.i(ZTag.TAG_DEBUG, "${ZDeviceUtil.getSdFreeSpace() < 5L shl 30}")
        ZLog.i(ZTag.TAG_DEBUG, "${ZDeviceUtil.getSdFreeSpace() < 10L shl 30}")
        // --------------------------------------------------
//        AppUtil.startApplicationSimple(MyApplication.getInstance(), "com.xuehai.response_launcher_teacher")
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