package com.zaze.demo.debug

import com.zaze.common.widget.CustomToast
import com.zaze.core.network.http.ZRequest
import com.zaze.core.network.http.ZResponse
import com.zaze.utils.interceptor.Interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:45
 */
class HttpToastInterceptor : Interceptor<ZRequest, ZResponse> {
    override fun intercept(chain: Interceptor.Chain<ZRequest, ZResponse>): ZResponse {
        val request = chain.input()
        val response = chain.process(request)
        //
        if (!response.isSuccessful() && request.toast) {
            CustomToast.postShowToast("${response.message}(${response.code})")
        }
        return response
    }
}