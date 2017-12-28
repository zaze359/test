package com.zaze.demo.component.wifi.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.wifi.presenter.NetworkStatsPresenter;
import com.zaze.demo.component.wifi.view.NetworkStatsView;
import com.zaze.demo.debug.AnalyzeTrafficCompat;
import com.zaze.demo.debug.AppShortcut;
import com.zaze.demo.debug.ApplicationManager;
import com.zaze.demo.debug.NetTrafficStats;
import com.zaze.utils.ZStringUtil;

import java.util.Collection;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-22 04:36 1.0
 */
public class NetworkStatsPresenterImpl extends BaseMvpPresenter<NetworkStatsView> implements NetworkStatsPresenter {
    public NetworkStatsPresenterImpl(NetworkStatsView view) {
        super(view);
    }

    @Override
    public void onRefresh() {
        Observable.fromCallable(new Callable<Collection<NetTrafficStats>>() {
            @Override
            public Collection<NetTrafficStats> call() throws Exception {
                return AnalyzeTrafficCompat.getInstance(getView().getContext()).getDayNetworkTraffic();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Collection<NetTrafficStats>>() {
                    @Override
                    public void accept(Collection<NetTrafficStats> netTrafficStats) throws Exception {
                        if (isViewAttach()) {
                            getView().showNetworkStats(netTrafficStats);
                            getView().onRefreshCompleted();
                        }
                    }
                });
    }

    @Override
    public AppShortcut getApplicationByUid(String uid_tag_int) {
        if ("1000".equals(uid_tag_int)) {
            AppShortcut appShortcut = new AppShortcut();
            appShortcut.setName("系统应用");
            appShortcut.setPackageName("android");
            appShortcut.setUid(ZStringUtil.parseInt(uid_tag_int));
            return appShortcut;
        } else {
            return ApplicationManager.getAppShortcutByUid(ZStringUtil.parseInt(uid_tag_int));
        }
    }
}
