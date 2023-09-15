package com.zaze.demo.util

/**
 * Description : 单例构造器
 * @author : zaze
 * @version : 2022-11-30 20:41
 */
abstract class SingletonBuilder<T, P> {

    @Volatile
    private var instance: T? = null

    abstract fun create(params: P): T

    fun getInstance(params: P): T {
        return instance ?: synchronized(this) {
            instance ?: create(params).also {
                instance = it
            }
        }
    }
}

abstract class SimpleSingletonBuilder<T> : SingletonBuilder<T, Unit>() {
    override fun create(params: Unit): T {
        return create()
    }
    abstract fun create(): T

}