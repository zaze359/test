package com.zaze.demo.component.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-17 - 00:25
 */
public class GpsLocationListener implements LocationListener {

    private LocationListener locationListener;

    public GpsLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        ZLog.i(ZTag.TAG_DEBUG, "gpsLocationListener : onLocationChanged : " + location);
        if (location != null && locationListener != null) {
            locationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        ZLog.i(ZTag.TAG_DEBUG, "gpsLocationListener : onStatusChanged : " + provider);
        if (locationListener != null) {
            locationListener.onStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        ZLog.i(ZTag.TAG_DEBUG, "gpsLocationListener : onProviderEnabled : " + provider);
        if (locationListener != null) {
            locationListener.onProviderEnabled(provider);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        ZLog.i(ZTag.TAG_DEBUG, "gpsLocationListener : onProviderDisabled : " + provider);
        if (locationListener != null) {
            locationListener.onProviderDisabled(provider);
        }
    }


}
