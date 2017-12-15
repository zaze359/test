package com.zaze.demo.component.gps.view;


import com.zaze.common.base.BaseView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public interface GpsView extends BaseView {

    void showLocationInfo(double longitude, double latitude);

    void showProviderStatus(String str);

    void showAddress(String s);
}
