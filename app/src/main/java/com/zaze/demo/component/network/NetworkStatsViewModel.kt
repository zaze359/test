package com.zaze.demo.component.network

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.ext.set
import com.zaze.demo.component.network.compat.AnalyzeTrafficCompat
import com.zaze.demo.debug.NetTrafficStats
import com.zaze.common.util.plugins.rx.MyObserver
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-03-29 - 10:35
 */
class NetworkStatsViewModel(application: Application) : AbsAndroidViewModel(application) {

    val networkTraffic = MutableLiveData<Collection<NetTrafficStats>>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            networkTraffic.set(AnalyzeTrafficCompat.getInstance(application).dayNetworkTraffic)
            dragLoading.set(false)
        }
    }

//    fun onRefresh() {
//        Observable.fromCallable(Callable<Collection<Any>> { AnalyzeTrafficCompat.getInstance(getView().getContext()).getDayNetworkTraffic() }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Consumer<Collection<NetTrafficStats>> {
//                    @Throws(Exception::class)
//                    override fun accept(netTrafficStats: Collection<NetTrafficStats>) {
//                        if (isViewAttach()) {
//                            getView().showNetworkStats(netTrafficStats)
//                            getView().onRefreshCompleted()
//                        }
//                    }
//                })
//    }
//
//    fun getApplicationByUid(uid_tag_int: String): AppShortcut? {
//        if ("1000" == uid_tag_int) {
//            val appShortcut = AppShortcut()
//            appShortcut.setName("系统应用")
//            appShortcut.setPackageName("android")
//            appShortcut.setUid(ZStringUtil.parseInt(uid_tag_int))
//            return appShortcut
//        } else {
//            return ApplicationManager2.getAppShortcutByUid(ZStringUtil.parseInt(uid_tag_int))
//        }
//    }
}