package com.zaze.demo.component.wifi.contract;

import android.net.wifi.ScanResult;

import com.zaze.common.base.BasePresenter;
import com.zaze.common.base.BaseView;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-06-15 - 13:43
 */
public interface WifiContract {


    interface View extends BaseView {

        /**
         * 显示wifi列表
         *
         * @param list list
         */
        void showWifiList(List<ScanResult> list);
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * 获取网络状态
         */
        void getNetworkState();

        /**
         * 开始扫描
         */
        void startScan();
    }
}
