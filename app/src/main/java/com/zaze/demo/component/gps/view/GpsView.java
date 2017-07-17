package com.zaze.demo.component.gps.view;

import com.zaze.aarrepo.commons.base.ZBaseView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public interface GpsView extends ZBaseView {

    void showLocationInfo(double longitude, double latitude);

    void showProviderStatus(String str);

    void showAddress(String s);
}
