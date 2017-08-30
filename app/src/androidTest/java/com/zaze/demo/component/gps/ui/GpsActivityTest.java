package com.zaze.demo.component.gps.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-10 - 14:26
 */
@RunWith(AndroidJUnit4.class)
public class GpsActivityTest extends ActivityInstrumentationTestCase2<GpsActivity> {

    private GpsActivity activity;
    private LocationManager locationManager;

    public GpsActivityTest() {
        // 所有的ActivityInstrumentationTestCase2子类都需要调用该父类的super(String)构造方法
        super(GpsActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Test
    public void testGPS() {
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        ZLog.v(ZTag.TAG_DEBUG, "最佳的定位方式:" + bestProvider);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 1, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ZLog.v(ZTag.TAG_DEBUG, "location: %s", location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                ZLog.v(ZTag.TAG_DEBUG, "provider: %s, status: %d, extras: %s", provider, status, extras);
            }

            @Override
            public void onProviderEnabled(String provider) {
                ZLog.v(ZTag.TAG_DEBUG, "provider: %s", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                ZLog.v(ZTag.TAG_DEBUG, "provider: %s", provider);
            }
        });

    }
}