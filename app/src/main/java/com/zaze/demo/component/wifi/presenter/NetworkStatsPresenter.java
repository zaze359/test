package com.zaze.demo.component.wifi.presenter;

import com.zaze.common.base.BasePresenter;
import com.zaze.demo.component.wifi.view.NetworkStatsView;
import com.zaze.demo.debug.AppShortcut;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-22 04:36 1.0
 */
public interface NetworkStatsPresenter extends BasePresenter<NetworkStatsView> {

    void onRefresh();

    AppShortcut getApplicationByUid(String uid_tag_int);
}
