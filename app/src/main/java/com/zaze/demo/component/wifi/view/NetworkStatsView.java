package com.zaze.demo.component.wifi.view;

import com.zaze.common.base.BaseView;
import com.zaze.demo.debug.NetTrafficStats;

import java.util.Collection;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-22 04:36 1.0
 */
public interface NetworkStatsView extends BaseView {

    /**
     * 显示网络统计
     *
     * @param netTrafficStats netTrafficStats
     */
    void showNetworkStats(Collection<NetTrafficStats> netTrafficStats);

    /**
     * 刷新完成时
     */
    void onRefreshCompleted();
}
