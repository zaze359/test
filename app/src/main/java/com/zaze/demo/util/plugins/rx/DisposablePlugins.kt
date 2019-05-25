package com.zaze.demo.util.plugins.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Description : Disposable 管理程序
 * @author : ZAZE
 * @version : 2018-12-22 - 22:50
 */
object DisposablePlugins {
    /**
     * 可以统一dispose的
     */
    private val canClearDisposable = CompositeDisposable()

    @JvmStatic
    fun add(d: Disposable?, compositeDisposable: CompositeDisposable? = null) {
        d?.let {
            compositeDisposable?.add(it) ?: canClearDisposable.add(it)
        }
    }

    @JvmStatic
    fun remove(d: Disposable?, compositeDisposable: CompositeDisposable? = null) {
        d?.let {
            compositeDisposable?.remove(it) ?: canClearDisposable.remove(it)
        }
    }

    @JvmStatic
    fun clear() {
        canClearDisposable.clear()
    }
}