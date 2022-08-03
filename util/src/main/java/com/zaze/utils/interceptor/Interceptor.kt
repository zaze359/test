package com.zaze.utils.interceptor

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-25 15:29
 */
interface Interceptor<IN, OUT> {

    fun intercept(chain: Chain<IN, OUT>): OUT

    interface Chain<INPUT, OUTPUT> {
        fun input(): INPUT
        fun process(input: INPUT): OUTPUT
    }
}