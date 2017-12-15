package com.zaze.demo.component.wifi.presenter.impl;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.wifi.presenter.WifiPresenter;
import com.zaze.demo.component.wifi.view.WifiView;
import com.zaze.utils.ZNetUtil;

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
