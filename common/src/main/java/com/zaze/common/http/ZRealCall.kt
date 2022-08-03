package com.zaze.common.http

import com.zaze.common.http.interceptors.HttpLogInterceptor
import com.zaze.utils.interceptor.Interceptor
import com.zaze.utils.interceptor.RealInterceptorChain

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:42
 */
class ZRealCall(val request: ZRequest) {
    private val interceptors = ArrayList<Interceptor<ZRequest, ZResponse>>()

    /**
     * 添加拦截器
     *
     * @param interceptor interceptor
     * @return Builder
     */
    fun addInterceptor(interceptor: Interceptor<ZRequest, ZResponse>): ZRealCall {
        interceptors.add(interceptor)
        return this
    }

    fun addInterceptors(interceptors: List<Interceptor<ZRequest, ZResponse>>?): ZRealCall {
        interceptors?.let {
            this.interceptors.addAll(it)
        }
        return this
    }

    fun getResponseWhitChain(): ZResponse {
        val interceptorList = ArrayList<Interceptor<ZRequest, ZResponse>>()
        interceptorList.addAll(interceptors)
        interceptorList.add(HttpLogInterceptor())
        interceptorList.add(RealRequest())
        return RealInterceptorChain(interceptorList, request).process(request)
    }

    fun execute(): ZResponse {
        return getResponseWhitChain()
    }
}