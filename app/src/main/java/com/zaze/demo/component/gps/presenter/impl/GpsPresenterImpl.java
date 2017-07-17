package com.zaze.demo.component.gps.presenter.impl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.app.MyApplication;
import com.zaze.demo.component.gps.GpsLocationListener;
import com.zaze.demo.component.gps.NetLocationListener;
import com.zaze.demo.component.gps.presenter.GpsPresenter;
import com.zaze.demo.component.gps.view.GpsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public class GpsPresenterImpl extends ZBasePresenter<GpsView> implements GpsPresenter {

    private LocationManager locationManager;
    private GpsLocationListener gpsLocationListener;
    private NetLocationListener netLocationListener;
    private Location currentLocation = null;
    private Geocoder geocoder;

    public GpsPresenterImpl(GpsView view) {
        super(view);
        locationManager = (LocationManager) MyApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(MyApplication.getInstance(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
//        List<String> providers = locationManager.getProviders(true);
//        for (String str : providers) {
//            if (builder.length() != 0) {
//                builder.append(", ");
//            }
//            builder.append(str);
//        }
//        ZLog.i(ZTag.TAG_DEBUG, builder.toString());

        // --------------------------------------------------
    }

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

    }

    @Override
    public void start() {
        ZLog.i(ZTag.TAG_DEBUG, "判断GPS功能是否正常启动");
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ZLog.i(ZTag.TAG_DEBUG, "开启GPS导航...");
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ActivityUtil.startActivityForResult(view.getContext(), intent, 1);
            return;
        }
        // 有GPS_PROVIDER和NETWORK_PROVIDER两种
//        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
//        ZLog.v(ZTag.TAG_DEBUG, bestProvider);
        //
//        updateLocation(locationManager.getLastKnownLocation(bestProvider));
        //
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ZLog.e(ZTag.TAG_ERROR, "缺少权限");
            return;
        }
        requestGpsLocationListener();
        requestNetLocationUpdates();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            locationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
//                @Override
//                public void onSatelliteStatusChanged(GnssStatus status) {
//                    super.onSatelliteStatusChanged(status);
//                    updateGpsStatus(StringUtil.format("onSatelliteStatusChanged: %s", status));
//                }
//            });
//        } else {
//            locationManager.addGpsStatusListener(new GpsStatus.Listener() {
//                @Override
//                public void onGpsStatusChanged(int event) {
//                    String desc = StringUtil.format("onGpsStatusChanged event: %s", event);
//                    switch (event) {
//                        case GpsStatus.GPS_EVENT_FIRST_FIX:
//                            desc = "第一次定位";
//                            break;
//                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                            desc = "卫星状态改变";
//                            break;
//                        case GpsStatus.GPS_EVENT_STARTED:
//                            desc = "定位启动";
//                            break;
//                        case GpsStatus.GPS_EVENT_STOPPED:
//                            desc = "定位结束";
//                            break;
//                        default:
//                            break;
//                    }
////                    updateGpsStatus(desc);
//                }
//            });
//
//            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
//            //获取默认最大卫星数
//            int maxSatellites = gpsStatus.getMaxSatellites();
//            //获取第一次定位时间（启动到第一次定位）
//            int costTime = gpsStatus.getTimeToFirstFix();
//            //获取卫星
//            Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
//            int count = 0;
//            while (iters.hasNext() && count <= maxSatellites) {
//                GpsSatellite s = iters.next();
//                count++;
//            }
//            onLocationChanged("搜索到卫星:" + count);
//    }

    }

    private void requestGpsLocationListener() {
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 2, 5, gpsLocationListener);
    }

    private void requestNetLocationUpdates() {
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, netLocationListener);
    }

    private void removeNetUpdates() {
        if (netLocationListener != null) {
            locationManager.removeUpdates(netLocationListener);
            netLocationListener = null;
        }
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    public void stop() {
//        if (view.getContext() != null) {
//            if (ActivityCompat.checkSelfPermission(mContext.get(), Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext.get(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            locationManager.removeUpdates(mGPSLocation);
//        }
    }


    // --------------------------------------------------
    private String getAddress(Location location) {
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
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new
     *                            one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
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
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    // --------------------------------------------------
    private void updateLocation(Location location) {
        if (location != null) {
            currentLocation = location;
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            view.showLocationInfo(longitude, latitude);
            view.showAddress(getAddress(location));
        }
    }

    private void updateGpsStatus(String str) {
        view.showProviderStatus(str);
    }

}
