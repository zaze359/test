package com.zaze.utils.regular

import com.zaze.utils.http.HttpUrl
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
            Pattern.compile("(?<schema>http[s]?://)?(?<host>[a-z0-9A-Z\\-_.*]+)(?<port>:\\d+)?((?=/).*)?")
    }

    val matcher: Matcher

    init {
        matcher = URL_PATTER.matcher(url)
    }

    fun isMatched(): Boolean {
        return matcher.matches() && matcher.groupCount() > 2 && !matcher.group(2).isNullOrEmpty()
    }

    fun getDomain(): String {
        if (matcher.matches() && matcher.groupCount() > 2) {
            return matcher.group(2) ?: ""
        }
        return ""
    }
}