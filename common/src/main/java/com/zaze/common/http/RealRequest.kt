package com.zaze.common.http

import com.zaze.common.http.okhttp.OkHttpRequestClient
import com.zaze.utils.interceptor.Interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:31
 */
class RealRequest : Interceptor<ZRequest, ZResponse> {
    private val requestClient = OkHttpRequestClient()

    override fun intercept(chain: Interceptor.Chain<ZRequest, ZResponse>): ZResponse {
        val request = chain.input()
        val response = ZResponse(request)
        if (request.url.isEmpty()) {
            return response.setError(-1, "请求的URL不可为空")
        }
        return requestClient.request(request)
    }
}