package com.zaze.component.gps.view;

import android.location.LocationManager;

import com.zaze.aarrepo.commons.base.ZBaseView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public interface GpsView extends ZBaseView {
    LocationManager getLocationManager();

    void showLocationInfo(double longitude, double latitude);

    void showGpsStatus(String str);
}
