package com.zaze.utils.ext

import android.os.Build
import com.zaze.utils.http.HttpUrl
import com.zaze.utils.regular.UrlRegular
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Description : http 请求相关扩展方法
 * @author : zaze
 * @version : 2022-12-10 20:18
 */

/**
 *  构建给请求完整URL
 *  @param map 参数列表
 */
fun String.buildGetRequest(map: Map<String, String>?): String {
    if (map.isNullOrEmpty()) return this
    var url = this
    map.forEach {
        url += if (!url.contains("?")) {
            "?${it.key}=${it.value}}"
        } else {
            "&${it.key}=${it.value}}"
        }
    }
    return url
}

fun Map<String, String>.buildGetRequest(url: String): String {
    return url.buildGetRequest(this)
}

fun String?.parseHttpParams(): HttpUrl? {
    return toHttpUrl()
}


fun String?.urlEncode(): String {
    return this.urlEncodeOrNull() ?: ""
}

fun String?.urlDecode(): String {
    return this.urlDecodeOrNull() ?: ""
}

fun String?.urlDecodeOrNull(): String? {
    if (this.isNullOrEmpty()) return this
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            URLDecoder.decode(this, Charset.defaultCharset())
        }
        else -> {
            URLDecoder.decode(this)
        }
    }
}

fun String?.urlEncodeOrNull(): String? {
    if (this.isNullOrEmpty()) return this
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            URLEncoder.encode(this, Charset.defaultCharset())
        }
        else -> {
            URLEncoder.encode(this)
        }
    }
}

//
fun String?.toHttpUrl(): HttpUrl? {
    if (this.isNullOrEmpty()) {
        return null
    }
    val matcher = UrlRegular(this).matcher
    if (!matcher.matches()) {
        return null
    }
    val scheme = matcher.group(1)
    val host = if (matcher.groupCount() >= 2) {
        matcher.group(2)
    } else {
        null
    }
    val port = if (matcher.groupCount() >= 3) {
        matcher.group(3)
    } else {
        null
    }
    var pathStr: String? = null
    var paramStr: String? = null
    if (matcher.groupCount() >= 4) {
        matcher.group(4)?.split("?")?.also {
            pathStr = it[0]
        }?.takeIf {
            it.size == 2
        }?.let {
            paramStr = it[1]
        }
    }
    println("group4: ${matcher.group(4)}")
    return HttpUrl(
        scheme = scheme?.replace("://", "") ?: "http",
        host = host ?: "",
        port = port?.replace(":", "")?.toInt() ?: -1
    ).setPaths(pathStr).setParams(paramStr)
}