package com.zaze.utils.regular

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Description : 匹配http url
 * @author zaze
 * @version 2022/6/10 - 02:35
 */
@RequiresApi(Build.VERSION_CODES.O)
class UrlRegular(url: String) {s
    private companion object {
        private val URL_PATTER =
            Pattern.compile(
                "(?<schema>https?://)?" +          // 协议（可选）
                        "(?<host>[a-zA-Z0-9.-]+)" +        // 主机（域名/IP）
                        "(?<port>:\\d+)?" +                // 端口（可选）
                        "(?<path>/[^?#]*)?" +              // 路径（可选，/开头，到?/#为止）
                        "(?<params>\\?[^#]*)?"             // 参数（可选，?开头，到#为止）
            )
    }

    val matcher: Matcher = URL_PATTER.matcher(url)

    fun isMatched(): Boolean {
        return matcher.matches() && !matcher.group("schema").isNullOrEmpty()
    }

    fun getSchema(): String {
        if (matcher.matches()) {
            return matcher.group("schema") ?: ""
        }
        return ""
    }

    fun getHost(): String {
        if (matcher.matches()) {
            return matcher.group("host") ?: ""
        }
        return ""
    }

    fun getDomain(): String {
        return getHost()
    }

    fun getPort(): String {
        if (matcher.matches()) {
            return matcher.group("port") ?: ""
        }
        return ""
    }

    fun getPath(): String {
        if (matcher.matches()) {
            return matcher.group("path") ?: ""
        }
        return ""
    }

    fun getParams(): String {
        if (matcher.matches()) {
            return matcher.group("params") ?: ""
        }
        return ""
    }

    fun getPathAndParams(): String {
        return getPath() + getParams()
    }
}