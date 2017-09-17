package com.zaze.demo.debug

import android.util.Base64
import com.zaze.utils.ZEncryptionUtil
import com.zaze.utils.ZFileUtil
import com.zaze.utils.ZStringUtil
import com.zaze.utils.date.ZDateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
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
//        showLog("createDimens", { createDimens(1f, ZDisplayUtil.SCREEN_DENSITY) })
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
                builder.append(ZStringUtil.format("\t<dimen name=\"dp_$dp\">%1.1fdp</dimen>\n", dp * baseDensity / screenDensity))
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
        ZFileUtil.deleteFileByCmd(secretFile)
        val current = System.currentTimeMillis()
        var start = ZDateUtil.getDayStart(current) - ZDateUtil.DAY * 7
        val end = ZDateUtil.getDayEnd(current) + ZDateUtil.DAY * 3
        while (start < end) {
            val date = Date(start)
            ZFileUtil.writeToFile(secretFile, "${ZDateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss")} : ${createDeveloperToken(date)}\n", true)
            start += ZDateUtil.HOUR
        }
        return "finish"
    }

    fun createDeveloperToken(date: Date): String {
        val year = ZDateUtil.getYear(date)
        val month = ZDateUtil.getMonth(date).number
        val day = ZDateUtil.getDay(date)
        val hour = ZDateUtil.getHour(date)
        val secret = ZStringUtil.format(
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