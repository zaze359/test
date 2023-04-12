package com.zaze.core.network.http.interceptors

import com.zaze.core.network.http.ZRequest
import com.zaze.core.network.http.ZResponse
import com.zaze.utils.interceptor.Interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:45
 */
class HttpLogInterceptor : Interceptor<ZRequest, ZResponse> {

    override fun intercept(chain: Interceptor.Chain<ZRequest, ZResponse>): ZResponse {
        val request = chain.input()
        if (request.printLog) {
            request.log()
        }
        val response = chain.process(request)
        if (request.printLog) {
            response.log()
        }
        return response
    }
}