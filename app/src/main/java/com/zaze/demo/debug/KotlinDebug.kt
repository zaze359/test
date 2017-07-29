package com.zaze.demo.debug

import android.util.Base64
import com.zaze.aarrepo.commons.date.DateUtil
import com.zaze.aarrepo.commons.log.ZLog
import com.zaze.aarrepo.utils.EncryptionUtil
import com.zaze.aarrepo.utils.FileUtil
import com.zaze.aarrepo.utils.StringUtil
import com.zaze.aarrepo.utils.ZAppUtil
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
        return showLog("createDeveloperAccount", { createDeveloperToken() })
//        return showLog("clearCacheData", { clearCacheData() })
//        return showLog("searchFile", { searchFile() })
//        return "${System.currentTimeMillis()} : ${System.currentTimeMillis() % 10000L}"
    }

    fun showLog(msg: String, body: () -> String): String {
        val print = body()
        ZLog.i(msg, print)
        return print
    }


    // --------------------------------------------------
    /**
     * Description : 获取开发者Token
     * @author zaze
     * @version 2017/6/22 - 上午10:25 1.0
     */
    fun createDeveloperToken(): String {
//        date = DateUtil.stringToDate("2017-06-23 18:55:55", "yyyy-mm-dd hh:MM:ss")
        val current = System.currentTimeMillis()
        var start = DateUtil.getDayStart(current)
        val end = DateUtil.getDayEnd(current) + DateUtil.DAY * 10
        val builder = StringBuilder()
        while (start < end) {
            val date = Date(start)
            builder.append("\n")
            builder.append(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"))
            builder.append(" : ${createDeveloperToken(date)}")
            start += DateUtil.HOUR
        }

        return builder.toString()
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
        val md5 = EncryptionUtil.getMD5(Base64.encode(secret.toByteArray(), Base64.DEFAULT))

//        when (hour) {
//            in 0..6 -> return md5.substring(0, 8)
//            in 6..12 -> return md5.substring(8, 16)
//            in 12..18 -> return md5.substring(16, 24)
//            else -> return md5.substring(24, 32)
//        }
        when (hour) {
            in 12..24 -> return md5.substring(0, 16)
            else -> return md5.substring(16, 32)
        }
    }

    // --------------------------------------------------

    fun print(): String {
        var serial: String
        //        val packageInfo = ZBaseApplication.getInstance().packageManager.getPackageArchiveInfo(
//                "${FileUtil.getSDCardRoot()}xuehai/download/app/com.xuehai.launcher/1/com.xuehai.launcher.apk"
//                , 0)
//        serial = packageInfo.toString()
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (e: Exception) {
            e.printStackTrace()
            serial = "Exception"
        }
        return serial
    }

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