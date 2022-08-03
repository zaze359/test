package com.zaze.utils.regular

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Description : 匹配http url
 * @author zaze
 * @version 2022/6/10 - 02:35
 */
class UrlRegular internal constructor(url: String) {
    private companion object {
        private val URL_PATTER =
            Pattern.compile("(http[s]?://)?([a-z0-9A-Z\\-_.*]+)(:\\d+)?((?=(/)).*)?")
    }

    private val matcher: Matcher

    init {
        matcher = URL_PATTER.matcher(url)
    }

    fun isMatched(): Boolean {
        return matcher.matches() && matcher.groupCount() > 2 && !isEmpty(matcher.group(2))
    }

    fun getDomain(): String {
        if (matcher.matches() && matcher.groupCount() > 2) {
            return matcher.group(2)
        }
        return ""
    }

    private fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.isEmpty()
    }
}