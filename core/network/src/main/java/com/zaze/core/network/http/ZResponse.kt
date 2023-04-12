package com.zaze.core.network.http

import com.zaze.utils.JsonUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import org.json.JSONException
import java.lang.StringBuilder

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-12-21 - 00:37
 */
class ZResponse(var request: ZRequest) {
    var code = 0
    var responseBody: String? = null
    var headers: Map<String, Any>? = null
    var message: String? = null
    // --------------------------------------------------
    /**
     * 输出log
     */
    fun log() {
        val builder = StringBuilder()
        builder.append("url: ").append(request.url)
            .append("\nresponseBody: ").append(responseBody)
            .append("\nmessage: ").append(message)
            .append("\nheaders: ")
        try {
            builder.append(JsonUtil.mapToJson(headers).toString(3))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        ZLog.v(ZTag.TAG_HTTP, builder.toString())
    }
    // --------------------------------------------------
    /**
     * 是否成功
     *
     * @return true成功
     */
    fun isSuccessful(): Boolean = code in 200..299

    fun isError(): Boolean = !isSuccessful()

    /**
     * Session 是否失效
     *
     * @return boolean
     */
    fun isSessionFailed(): Boolean = 107000401 == code

    /**
     * 从异常中拷贝数据
     *
     * @param throwable throwable
     * @return 变更后的LResponse
     */
    fun copyFrom(throwable: Throwable): ZResponse {
        code = -1
        responseBody = throwable.message
        return this
    }

    fun setError(
        errorCode: Int,
        errorMessage: String?
    ): ZResponse {
        this.code = errorCode
        this.responseBody = errorMessage
        return this
    }
}