package com.zaze.demo.component.wifi.view;

import android.net.wifi.ScanResult;

import com.zaze.common.base.BaseView;

import java.util.List;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public interface WifiView extends BaseView {

    /**
     * 显示wifi列表
     *
     * @param list list
     */
    void showWifiList(List<ScanResult> list);
}
