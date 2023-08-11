package com.zaze.common.util.plugins.rx

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-12-22 - 22:48
 */
open class MySingleObserver<T>(private val compositeDisposable: CompositeDisposable? = null) : SingleObserver<T> {
    var disposable: Disposable? = null

    override fun onSuccess(result: T) {
        DisposablePlugins.remove(disposable, compositeDisposable)
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        DisposablePlugins.add(disposable, compositeDisposable)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        DisposablePlugins.remove(disposable, compositeDisposable)
    }
}