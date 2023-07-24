package com.zaze.utils.interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:30
 */
open class RealInterceptorChain<IN, OUT>(
    private val interceptors: List<Interceptor<IN, OUT>>,
    private val input: IN,
    private val index: Int = 0
) :
    Interceptor.Chain<IN, OUT> {
    override fun input(): IN {
        return input
    }

    override fun proceed(input: IN): OUT {
        if (index >= interceptors.size) {
            throw AssertionError("index${index} >= ${interceptors.size}")
        }
        val next = RealInterceptorChain(interceptors, input, index + 1)
        return interceptors[index].intercept(next)
    }

}