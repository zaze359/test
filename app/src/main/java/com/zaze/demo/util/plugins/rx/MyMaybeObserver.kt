package com.zaze.common.plugins.rx

import io.reactivex.MaybeObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-12-22 - 23:40
 */
open class MyMaybeObserver<T>(private val compositeDisposable: CompositeDisposable? = null) : MaybeObserver<T> {
    var disposable: Disposable? = null

    override fun onSuccess(result: T) {
        DisposablePlugins.remove(disposable, compositeDisposable)
    }

    override fun onComplete() {
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