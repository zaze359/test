package com.zaze.demo.debug

import android.util.Base64
import com.zaze.aarrepo.commons.date.DateUtil
import com.zaze.aarrepo.commons.log.ZLog
import com.zaze.aarrepo.utils.*
import com.zaze.demo.app.MyApplication
import java.io.File
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
class KotlinDebug {

    fun test(): String {
//        return showLog("print", { print() })
//        return showLog("createDimens", { createDimens(1f, LocalDisplay.SCREEN_DENSITY) })
        createDeveloperToken()
//        return showLog("createDeveloperAccount", { createDeveloperToken() })
//        return showLog("clearCacheData", { clearCacheData() })
//        return showLog("searchFile", { searchFile() })
        return ""
    }

    private fun createDimens(baseDensity: Float, screenDensity: Float): String {
        var dp = 1
        val builderBase = StringBuilder()
        val builder = StringBuilder()
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        builder.append("<resources>\n")
        builderBase.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        builderBase.append("<resources>\n")
        do {
            if (dp <= 10 || dp % 2 == 0) {
                builder.append(StringUtil.format("\t<dimen name=\"dp_$dp\">%1.1fdp</dimen>\n", dp * baseDensity / screenDensity))
                builderBase.append("\t<dimen name=\"dp_$dp\">${dp}dp</dimen>\n")
            }
            dp++
        } while (dp <= 2000)
        builder.append("</resources>")
        builderBase.append("</resources>")
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/z_dimens/dimens.xml", builder.toString())
        ZFileUtil.writeToFile("${ZFileUtil.getSDCardRoot()}/z_dimens/dimens_base.xml", builderBase.toString())
        return builder.toString()
    }

    fun showLog(msg: String, body: () -> String): String {
        val print = body()
        ZLog.i(msg, print)
        return print
    }


    // --------------------------------------------------


    val secretFile = "${ZFileUtil.getSDCardRoot()}/secret/secret.txt"
    /**
     * Description : 获取开发者Token
     * @author zaze
     * @version 2017/6/22 - 上午10:25 1.0
     */
    fun createDeveloperToken(): String {
//        ZLog.e(ZTag.TAG_DEBUG, EncryptionUtil.getMD5("KiN4dWVoYWkyMDE3JnpoaXRvbmd5dW44JDEwMGZlbjI3QDAjKg=="))
//        ZLog.e(ZTag.TAG_DEBUG, EncryptionUtil.getMD5("KiN4dWVoYWkyMDE3JnpoaXRvbmd5dW44JDEwMGZlbjI3QDAjKg== "))
//        ZLog.e(ZTag.TAG_DEBUG, EncryptionUtil.getMD5("KiN4dWVoYWkyMDE3JnpoaXRvbmd5dW44JDEwMGZlbjI3QDAjKg==\n"))
        FileUtil.deleteFile(secretFile)
        val current = System.currentTimeMillis()
        var start = DateUtil.getDayStart(current)
        val end = DateUtil.getDayEnd(current) + DateUtil.DAY * 3
        while (start < end) {
            val date = Date(start)
            ZFileUtil.writeToFile(secretFile, "${DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss")} : ${createDeveloperToken(date)}\n", true)
            start += DateUtil.HOUR
        }
        return "finish"
    }

    fun createDeveloperToken(date: Date): String {
        val year = DateUtil.getYear(date)
        val month = DateUtil.getMonth(date).number
        val day = DateUtil.getDay(date)
        val hour = DateUtil.getHour(date)
        val secret = StringUtil.format(
                "*#xuehai%d&zhitongyun%d$100fen%d@%d#*",
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
        ZLog.i(ZTag.TAG_DEBUG, "base64 : ${base64.length}")
        val md5 = EncryptionUtil.getMD5(base64)
//        ZFileUtil.writeToFile(secretFile, "$secret,$base64,$md5\n", true)
        return md5
    }

    // --------------------------------------------------

    fun clearCacheData(): String {
        return "" + ZAppUtil.clearDataInfo(MyApplication.getInstance(), MyApplication.getInstance().packageName)
//        return "" + FileUtil.deleteFile("/data/data/" + MyApplication.getInstance().packageName)

    }

    // --------------------------------------------------
    fun searchFile(): String {
        val fileSet = FileUtil.searchFileLoop(File("/sdcard/"), "com.xuehai.response_launcher_teacher");
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