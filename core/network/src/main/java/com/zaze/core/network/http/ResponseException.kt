package com.zaze.core.network.http

import java.util.*

/**
 * Description : 响应异常
 * @author : ZAZE
 * @version : 2019-01-31 - 14:59
 */
class ResponseException(val response: ZResponse) : Exception() {

    override val message: String?
        get() = String.format(Locale.getDefault(), "%s(%d)", response.responseBody, response.code)
}