package com.zaze.utils.interceptor

/**
 * Description : 拦截器
 * @author : zaze
 * @version : 2022-05-25 15:29
 */
interface Interceptor<IN, OUT> {

    /**
     * 拦截处理
     */
    fun intercept(chain: Chain<IN, OUT>): OUT

    /**
     * 责任链
     */
    interface Chain<INPUT, OUTPUT> {
        /**
         * 获取请求的输入
         */
        fun input(): INPUT

        /**
         * 推进请求
         */
        fun proceed(input: INPUT): OUTPUT
    }
}