package com.zaze.demo.component.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.app.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-08-08 - 14:39
 */
public class LocationHelper {

    private static LocationManager locationManager = (LocationManager) ZBaseApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
    private static GpsLocationListener gpsLocationListener;
    private static NetLocationListener netLocationListener;
    private static Location currentLocation = null;
    private static final long minTime = 5000L;
    private static final float minDistance = 5f;

    public static void stop() {
        removeNetUpdates();
        removeGpsUpdates();
    }


    public static void start() {
        if (!checkAndroidPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && !checkAndroidPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ZLog.e(ZTag.TAG_DEBUG, "缺少权限");
            return;
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ZLog.i(ZTag.TAG_DEBUG, "GPS没有开启");
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            ActivityUtil.startActivityForResult(this, intent, 1);
        } else {
            ZLog.i(ZTag.TAG_DEBUG, "GPS功能正常启动");
        }
        requestGpsLocationListener();
        requestNetLocationUpdates();
    }

    private static void requestGpsLocationListener() {
        if (gpsLocationListener == null) {
            gpsLocationListener = new GpsLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (isBetterLocation(location, currentLocation)) {
                        updateLocation(location);
                    }
                    removeNetUpdates();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    updateGpsStatus(StringUtil.format("onStatusChanged: %s, status: %d, extras: %s", provider, status, extras));
                    if (LocationProvider.OUT_OF_SERVICE == status) {
                        updateGpsStatus("GPS服务丢失,切换至网络定位");
                        requestNetLocationUpdates();
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    updateGpsStatus(StringUtil.format("onProviderEnabled: %s", provider));
                }

                @Override
                public void onProviderDisabled(String provider) {
                    updateGpsStatus(StringUtil.format("onProviderDisabled: %s", provider));
                }
            });
        }
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsLocationListener);
        }
    }

    private static void removeGpsUpdates() {
        if (gpsLocationListener != null) {
            locationManager.removeUpdates(gpsLocationListener);
            gpsLocationListener = null;
        }
    }

    // --------------------------------------------------
    private static void requestNetLocationUpdates() {
        if (netLocationListener == null) {
            netLocationListener = new NetLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    updateGpsStatus(StringUtil.format("onStatusChanged: %s, status: %d, extras: %s", provider, status, extras));
                }

                @Override
                public void onProviderEnabled(String provider) {
                    updateGpsStatus(StringUtil.format("onProviderEnabled: %s", provider));
                }

                @Override
                public void onProviderDisabled(String provider) {
                    updateGpsStatus(StringUtil.format("onProviderDisabled: %s", provider));
                    removeNetUpdates();
                }
            });
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, 0, netLocationListener);
        }
    }

    private static void removeNetUpdates() {
        if (netLocationListener != null) {
            locationManager.removeUpdates(netLocationListener);
            netLocationListener = null;
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    private static void updateLocation(Location location) {
        if (location != null) {
            currentLocation = location;
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String locationInfo = StringUtil.format("经纬度 : (%s,%s)", longitude, latitude);
            ZLog.i(ZTag.TAG_DEBUG, locationInfo);
        }
    }

    private static void updateGpsStatus(String str) {
        ZLog.i(ZTag.TAG_DEBUG, str);
    }

    private static boolean checkAndroidPermission(String permission) {
        return ActivityCompat.checkSelfPermission(ZBaseApplication.getInstance(), permission) == PackageManager.PERMISSION_GRANTED;
    }


    // --------------------------------------------------
    private String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(MyApplication.getInstance(), Locale.getDefault());
        List<Address> addresses = null;
        StringBuilder curAddr = new StringBuilder();
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (addresses == null || addresses.isEmpty()) {
            ZLog.e(ZTag.TAG_ERROR, "没有找到相关地址!");
        } else {
            Address address = addresses.get(0);
            List<String> addressFragments = new ArrayList<>();

            String featureName = address.getFeatureName();
            ZLog.i(ZTag.TAG_DEBUG, "featureName : " + featureName);
//            if (!TextUtils.isEmpty(featureName)
//                    && !addressFragments.isEmpty()
//                    && !addressFragments.get(addressFragments.size() - 1).equals(featureName)) {
//                addressFragments.add(featureName);
//                curAddr.append(featureName);
//            }
//            updateShowData();
            curAddr.append(address.getCountryName()).append(", ");
            curAddr.append(address.getAdminArea()).append(", ");
            curAddr.append(address.getLocality()).append(", ");
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                String addrLine = address.getAddressLine(i);
                addressFragments.add(addrLine);
                ZLog.i(ZTag.TAG_DEBUG, "addrLine : " + addrLine);
                if (curAddr.length() != 0) {
                    curAddr.append("\n");
                }
                curAddr.append(addrLine);
            }
            ZLog.i(ZTag.TAG_DEBUG, "详情地址已经找到,地址:" + curAddr);
        }
        return curAddr.toString();
    }


    // --------------------------------------------------
    // --------------------------------------------------
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new
     *                            one
     */
    protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}