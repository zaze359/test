package com.zaze.core.network.http

import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.json.JSONException

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-26 - 12:35
 */
data class ZRequest(
    val url: String = "",
    val method: String = RequestMethod.GET,
    val headers: HashMap<String, String> = HashMap<String, String>().also { map ->
        if (map.isEmpty()) {
            map["Content-Type"] = "application/json"
            map["Accept"] = "*/*"
            map["User-Agent"] = ""
            map["Authorization"] = ""
        }
    },
    val requestBody: ZRequestBody? = null,
    // --------------------------------------------------
    val savePath: String? = null,
    val md5: String? = null,
    val toast: Boolean = true,
    /**
     * 输出日志
     */
    val printLog: Boolean = true
) {
    /**
     * second
     */
    var connectTimeout: Long = 10

    /**
     * second
     */
    var readTimeout: Long = 10

    /**
     * second
     */
    var writeTimeout: Long = 10

//    fun url(url: String, map: Map<String, String>? = null): Builder {
//        this.url = HttpUtil.buildGetRequest(url, map)
//    }

    /**
     * 设置超时时间
     * [timeSecond] 秒
     */
    fun setTimeout(timeSecond: Long) {

        if (timeSecond > 0) {
            this.connectTimeout = timeSecond
            this.readTimeout = timeSecond
            this.writeTimeout = timeSecond
        }
    }

    /**
     * 输出log
     */
    fun log() {
        val builder = StringBuilder()
        builder.append("url: ").append(url)
            .append("\nmethod: ").append(method)
            .append("\nrequestBody: ").append(requestBody)
            .append("\nheaders: ")
        try {
            builder.append(JsonUtil.mapToJson(headers as Map<String, Any>?).toString(3))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        ZLog.v(ZTag.TAG_HTTP, builder.toString())
    }

    // --------------------------------------------------

    // --------------------------------------------------
    object RequestMethod {
        val GET = "GET"
        val POST = "POST"
        val PUT = "PUT"
        val PATCH = "PATCH"
        val DELETE = "DELETE"
    }
}
