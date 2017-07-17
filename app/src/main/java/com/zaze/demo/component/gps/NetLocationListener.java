package com.zaze.demo.component.gps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-17 - 00:25
 */
public class NetLocationListener implements LocationListener {

    private LocationListener locationListener;

    public NetLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        ZLog.i(ZTag.TAG_DEBUG, "NetLocationListener : onLocationChanged : " + location);
        if (location != null && locationListener != null) {
            locationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        ZLog.i(ZTag.TAG_DEBUG, "NetLocationListener : onStatusChanged : " + provider);
        switch (status) {
            case LocationProvider.AVAILABLE:
                break;
            case LocationProvider.OUT_OF_SERVICE:
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
        }
        if (locationListener != null) {
            locationListener.onStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        ZLog.i(ZTag.TAG_DEBUG, "NetLocationListener : onProviderEnabled : " + provider);
        if (locationListener != null) {
            locationListener.onProviderEnabled(provider);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        ZLog.i(ZTag.TAG_DEBUG, "NetLocationListener : onProviderDisabled : " + provider);
        if (locationListener != null) {
            locationListener.onProviderDisabled(provider);
        }
    }


}
