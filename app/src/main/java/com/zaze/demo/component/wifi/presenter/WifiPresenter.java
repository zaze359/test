package com.zaze.demo.component.wifi.presenter;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public interface WifiPresenter {

    /**
     * 获取网络状态
     */
    void getNetworkState();

    /**
     * 开始扫描
     */
    void startScan();

    /**
     * 获取网络列表
     */
    void getScanResults();

}
