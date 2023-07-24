package com.zaze.core.network.exception

import com.google.gson.JsonParseException
import com.zaze.core.network.http.ZResponse
import org.json.JSONException
import java.util.*

/**
 * Description : 响应异常
 * @author : ZAZE
 * @version : 2019-01-31 - 14:59
 */
class ResponseException(val response: ZResponse) : Exception() {
    companion object {
        fun handlerException(response: ZResponse, e: Throwable): ResponseException {
            return when (e) {
                is ResponseException -> {
                    e
                }
                is JsonParseException, is JSONException -> {
                    // 解析异常
                    ResponseException(response)
                }
                else -> {
                    ResponseException(response)
                }
            }
        }
    }

    override val message: String
        get() = String.format(Locale.getDefault(), "%s(%d)", response.responseBody, response.code)


}