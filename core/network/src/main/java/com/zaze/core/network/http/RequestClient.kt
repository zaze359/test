package com.zaze.core.network.http

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-12 - 10:56
 */
interface RequestClient {

    fun request(request: ZRequest): ZResponse

}