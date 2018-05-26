package com.zaze.demo.debug

import android.os.SystemClock
import android.util.Base64
import com.zaze.utils.ZEncryptionUtil
import com.zaze.utils.ZFileUtil
import com.zaze.utils.ZStringUtil
import com.zaze.utils.config.ZConfigHelper
import com.zaze.utils.date.ZDateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.util.*
import kotlin.collections.HashMap


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
        result += (System.currentTimeMillis() - SystemClock.elapsedRealtime())
        val file = File("/sdcard/test.ini")
        val config = ZConfigHelper.newInstance(file)
        val map = HashMap<String, String>()
        for (i in 0..100000) {
            map.put("${System.currentTimeMillis()}" + i, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
        config.setProperty(map)
//        createDeveloperToken()
        ZLog.i(ZTag.TAG_DEBUG, result)
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
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/zaze/z_dimens/dimens.xml", dpBuilder.toString())
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/zaze/z_dimens/dimens_base.xml", baseBuilder.toString())
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
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/zazen/values-sw${screenSw}dp/dimens.xml", swBuilder.toString())
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/zazen/values-sw${baseSw}dp/dimens.xml", baseBuilder.toString())
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

    val secretFile = "${ZFileUtil.getSDCardRoot()}/secret/secret.txt"

    /**
     * @author zaze
     * @version 2017/6/22 - 上午10:25 1.0
     */
    fun createDeveloperToken(): String {
        ZFileUtil.deleteFileByCmd(secretFile)
//        val current = System.currentTimeMillis()
        val current = ZDateUtil.stringToDate("2000-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").time
        var start = ZDateUtil.getDayStart(current)
        val end = ZDateUtil.getDayEnd(current) + ZDateUtil.YEAR * 30
        while (start < end) {
            val date = Date(start)
            ZFileUtil.writeToFile(secretFile, "${ZDateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss")} : ${createDeveloperToken(date)}\n", true)
            start += ZDateUtil.HOUR
        }
        return "finish"
    }

    fun createDeveloperToken(date: Date): String {
        val secretKey = "%s%s%s%s"
        val year = ZDateUtil.getYear(date)
        val month = ZDateUtil.getMonth(date).number
        val day = ZDateUtil.getDay(date)
        val hour = ZDateUtil.getHour(date)
        val secret = ZStringUtil.format(
                secretKey,
                year, month, day, hour
        )
        val md5 = writeKeyToFile(secret)
        val key: String
        when (hour) {
            in 12..24 -> key = md5.substring(0, 16)
            else -> key = md5.substring(16, 32)
        }
        return key
    }

    private fun writeKeyToFile(secret: String): String {
        ZLog.i(ZTag.TAG_DEBUG, "secret : $secret")
        val base64 = String(Base64.encode(secret.toByteArray(), Base64.DEFAULT))
        ZLog.i(ZTag.TAG_DEBUG, "base64 : $base64")
        val md5 = ZEncryptionUtil.getMD5(base64)
//        ZFileUtil.writeToFile(secretFile, "$secret,$base64,$md5\n", true)
        return md5
    }

    // --------------------------------------------------

    fun clearCacheData(): String {
//        ZAppUtil.clearDataInfo(MyApplication.getInstance(), MyApplication.getInstance().packageName)
//        return "" + FileUtil.deleteFile("/data/data/" + MyApplication.getInstance().packageName)
        return ""
    }

    // --------------------------------------------------
    fun searchFile(): String {
        val fileSet = ZFileUtil.searchFileLoop(File("/sdcard/"), "com.xuehai.response_launcher_teacher");
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