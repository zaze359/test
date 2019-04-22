package com.zaze.common.base.ext

import androidx.lifecycle.MutableLiveData


/**
 * Description : LiveData 扩展 dataBinding observable set get 方式调用
 * @author : ZAZE
 * @version : 2018-12-02 - 13:57
 */

fun <T : Any> MutableLiveData<T>.set(value: T? = null) = postValue(value)

fun <T : Any> MutableLiveData<T>.get() = value
