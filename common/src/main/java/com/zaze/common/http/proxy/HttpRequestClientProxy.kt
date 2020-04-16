package com.zaze.common.http.proxy

import com.zaze.common.base.BaseApplication
import com.zaze.common.http.RequestClient
import com.zaze.common.http.ZRequest
import com.zaze.common.http.ZResponse
import com.zaze.common.widget.CustomToast
import com.zaze.utils.NetUtil

/**
 * Description : Http请求代理
 * @author : ZAZE
 * @version : 2019-07-12 - 13:38
 */
class HttpRequestClientProxy(private val client: RequestClient) : RequestClient {

    override fun request(request: ZRequest): ZResponse {
        request.log()
        val response: ZResponse
        if (!NetUtil.isAvailable(BaseApplication.getInstance())) {
            response = ZResponse(request).also {
                it.code = -1
                it.message = "当前处于离线状态, 请连接可以网络"
            }
        } else {
            response = client.request(request)
        }
        if (!response.isSuccessful && request.toast) {
            CustomToast.postShowToast("${response.message}(${response.code})")
        }
        return response
    }
}