package com.zaze.demo.debug

import com.zaze.common.base.BaseApplication
import com.zaze.core.network.http.ZRequest
import com.zaze.core.network.http.ZResponse
import com.zaze.utils.NetUtil
import com.zaze.utils.interceptor.Interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:45
 */
class HttpNetworkInterceptor : Interceptor<ZRequest, ZResponse> {
    override fun intercept(chain: Interceptor.Chain<ZRequest, ZResponse>): ZResponse {
        val request = chain.input()
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
        return response
    }
}