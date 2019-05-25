package com.zaze.demo.util.plugins.rx

import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-12-22 - 23:01
 */
open class MyObserver<T>(private val compositeDisposable: CompositeDisposable? = null) : Observer<T> {
    var disposable: Disposable? = null
    override fun onComplete() {
        DisposablePlugins.remove(disposable, compositeDisposable)
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        DisposablePlugins.add(disposable, compositeDisposable)
    }

    override fun onNext(result: T) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        DisposablePlugins.remove(disposable, compositeDisposable)
    }
}