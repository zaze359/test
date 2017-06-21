package com.zaze.demo.debug

import com.zaze.aarrepo.commons.log.ZLog

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-06-07 - 14:53
 */
class KotlinDebug {


    fun test(): String {

        var serial: String
        //        val packageInfo = ZBaseApplication.getInstance().packageManager.getPackageArchiveInfo(
//                "${FileUtil.getSDCardRoot()}xuehai/download/app/com.xuehai.launcher/1/com.xuehai.launcher.apk"
//                , 0)
//        serial = packageInfo.toString()
        // --------------------------------------------------
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (e: Exception) {
            e.printStackTrace()
            serial = "Exception"
        }
        return showLog("aa", { print(serial) })
    }

    fun showLog(msg: String, body: (p0: String) -> String): String {
        val print = body(msg)
        ZLog.i(msg, print)
        return print
    }

    fun print(msg: String): String {
        return msg
    }
}