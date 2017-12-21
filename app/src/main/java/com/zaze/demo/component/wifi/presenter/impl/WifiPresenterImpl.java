package com.zaze.demo.component.wifi.presenter.impl;

import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.wifi.presenter.WifiPresenter;
import com.zaze.demo.component.wifi.view.WifiView;
import com.zaze.utils.ZAppUtil;
import com.zaze.utils.ZNetUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public class WifiPresenterImpl extends BaseMvpPresenter<WifiView> implements WifiPresenter {
    private WifiManager wifiManager;

    public WifiPresenterImpl(WifiView view) {
        super(view);
        wifiManager = ZNetUtil.getWifiManager(view.getContext());
    }

    @Override
    public void getNetworkState() {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getView().getContext().getSystemService(NETWORK_STAT_SERVICE);
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalRxBytes() : " + TrafficStats.getTotalRxBytes());
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalTxBytes() : " + TrafficStats.getTotalTxBytes());
        ZLog.i(ZTag.TAG_DEBUG, "TrafficStats.getTotalTxBytes() : " + TrafficStats.getUidTxBytes(ZAppUtil.INSTANCE.getApplicationInfo(getView().getContext(), null).uid));
    }

    @Override
    public void startScan() {
        // 扫描wifi
        wifiManager.startScan();
    }

    @Override
    public void getScanResults() {
        List<ScanResult> list = wifiManager.getScanResults();
        getView().showWifiList(list);
    }
}
