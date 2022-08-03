package com.zaze.utils.regular

import android.text.TextUtils
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Description : IP 匹配
 * @author zaze
 * @version 2022/6/10 - 02:35
 */
class IpRegular internal constructor(ip: String) {
    private companion object {
        private val IP_PATTER =
            Pattern.compile("(http[s]?://)?([\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3})(:\\d+)?((?=(/)).*)?")
    }

    private val matcher: Matcher

    init {
        matcher = IP_PATTER.matcher(ip)
    }

    fun isMatched(): Boolean {
        return matcher.matches() && matcher.groupCount() > 2 && !TextUtils.isEmpty(matcher.group(2))
    }

    fun getIpWithoutPort(): String {
        if (isMatched()) {
            return matcher.group(2)
        }
        return ""
    }

    fun getIpWithPort(): String {
        if (isMatched()) {
            return "${matcher.group(2)}${matcher.group(3)}"
        }
        return ""
    }

    fun getPort(): Int {
        if (isMatched()) {
            return try {
                matcher.group(3).replace(":", "").toInt()
            } catch (e: Exception) {
                80
            }
        } else {
            return 80
        }
    }

}