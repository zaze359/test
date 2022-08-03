package com.zaze.common.http.interceptors

import com.zaze.common.base.BaseApplication
import com.zaze.common.http.ZRequest
import com.zaze.common.http.ZResponse
import com.zaze.common.widget.CustomToast
import com.zaze.utils.NetUtil
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
        val response = if (!NetUtil.isAvailable(BaseApplication.getInstance())) {
            return ZResponse(request).also {
                it.code = -1
                it.message = "当前处于离线状态, 请连接可以网络"
            }
        } else {
            chain.process(request)
        }
        if (request.printLog) {
            response.log()
        }
        if (!response.isSuccessful() && request.toast) {
            CustomToast.postShowToast("${response.message}(${response.code})")
        }

        return response
    }
}