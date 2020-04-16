package com.zaze.common.http

import com.zaze.utils.HttpUtil
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
class ZRequest private constructor(builder: Builder) {
    val url = builder.url
    val method = builder.method
    val headers = builder.headers
    val requestBody = builder.requestBody
    // --------------------------------------------------
    val connectTimeout = builder.connectTimeout
    val readTimeout = builder.readTimeout
    val writeTimeout = builder.writeTimeout
    // --------------------------------------------------
    val toast = builder.toast
    // --------------------------------------------------
    val savePath = builder.savePath ?: ""
    val md5 = builder.md5

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

    class Builder {
        internal var url: String = ""
        internal var method: String = RequestMethod.GET
        internal var headers = HashMap<String, String>().also { map ->
            if (map.isEmpty()) {
                map["Content-Type"] = "application/json"
                map["Accept"] = "*/*"
                map["User-Agent"] = ""
                map["Authorization"] = ""
            }
        }
        internal var requestBody: ZRequestBody? = null
        internal var connectTimeout = 0
        internal var readTimeout = 0
        internal var writeTimeout = 0
        // --------------------------------------------------
        internal var savePath: String? = null
        internal var md5: String? = null
        // --------------------------------------------------
        internal var toast: Boolean = false
        /**
         * 保存错误日志
         */
        internal var saveError: Boolean = true

        /**
         * url + map 拼接
         *
         * @param url url
         * @param map map
         * @return Builder
         */
        fun method(method: String): Builder {
            this.method = method
            return this
        }

        /**
         * url + map 拼接
         *
         * @param url url
         * @param map map
         * @return Builder
         */
        @JvmOverloads
        fun url(url: String, map: Map<String, String>? = null): Builder {
            this.url = HttpUtil.buildGetRequest(url, map)
            return this
        }

        /**
         * header
         *
         * @param name  name
         * @param value value
         * @return Builder
         */
        fun header(name: String, value: String): Builder {
            try {
                headers[name] = value
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return this
        }

        /**
         * headers
         *
         * @param headers headers
         * @return Builder
         */
        fun headers(headers: Map<String, String>): Builder {
            this.headers.putAll(headers)
            return this
        }

        /**
         * requestBody
         *
         * @param requestBody requestBody
         * @return Builder
         */
        fun requestBody(requestBody: ZRequestBody): Builder {
            this.requestBody = requestBody
            return this
        }

        /**
         * 设置超时时间
         * [timeMillis] 毫秒
         */
        fun setTimeout(timeMillis: Int) {
            if (timeMillis > 0) {
                this.connectTimeout = timeMillis
                this.readTimeout = timeMillis
                this.writeTimeout = timeMillis
            }
        }

        // --------------------------------------------------
        /**
         * savePath
         *
         * @param savePath savePath
         * @return Builder
         */
        fun savePath(savePath: String): Builder {
            this.savePath = savePath
            return this
        }

        /**
         * md5
         *
         * @param md5 md5
         * @return Builder
         */
        fun md5(md5: String?): Builder {
            this.md5 = md5
            return this
        }

        // --------------------------------------------------
        /**
         * toast
         *
         * @param enable enable
         * @return Builder
         */
        fun toast(enable: Boolean): Builder {
            this.toast = enable
            return this
        }


        /**
         * 保存错误日志
         *
         * @param needSave true保存
         * @return Builder
         */
        fun saveError(needSave: Boolean): Builder {
            saveError = needSave
            return this
        }
        // --------------------------------------------------

        /**
         * build
         *
         * @return LRequest
         */
        fun build(): ZRequest {
            return ZRequest(this)
        }
    }
}
