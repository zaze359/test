package com.zaze.utils

/**
 * Description : 回调接口（）
 *
 * @author : ZAZE
 * @version : 2017-01-06 - 11:16
 */
abstract class ZCallback<D> {

    var message: String? = null

    open fun onError(errorCode: Int, errorMsg: String?) {}

    open fun preNext(d: D?): D? {
        return d
    }

    abstract fun onNext(d: D?)

    abstract fun onCompleted()
}
