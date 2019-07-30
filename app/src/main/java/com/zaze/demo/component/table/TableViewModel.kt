package com.zaze.demo.component.table

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.ext.set
import com.zaze.demo.model.ModelFactory
import com.zaze.demo.model.entity.TableEntity
import com.zaze.demo.util.plugins.rx.MyObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-07-30 - 14:53
 */
class TableViewModel(application: Application) : AbsAndroidViewModel(application) {
    private val entityModel = ModelFactory.getEntityModel()
    val data = MutableLiveData<List<TableEntity>>()

    fun refresh() {
        Observable.fromCallable { entityModel.tableList }.subscribeOn(Schedulers.io())
                .map {
                    data.set(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MyObserver(compositeDisposable))
    }
}