package com.zaze.demo.component.gps.presenter.impl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.component.gps.presenter.GpsPresenter;
import com.zaze.demo.component.gps.view.GpsView;

import java.util.Iterator;
import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public class GpsPresenterImpl extends ZBasePresenter<GpsView> implements GpsPresenter {

    private LocationManager locationManager;

    public GpsPresenterImpl(GpsView view) {
        super(view);
        locationManager = view.getLocationManager();
        StringBuilder builder = new StringBuilder();
        List<String> providers = locationManager.getProviders(true);
        for (String str : providers) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(str);
        }
        ZLog.i(ZTag.TAG_DEBUG, builder.toString());
    }

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

    }

    @Override
    public void start() {
        LogKit.v("判断GPS是否正常启动");
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LogKit.v("请开启GPS导航...");
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ActivityUtil.startActivityForResult(view.getContext(), intent, 0);
            return;
        }
        // 有GPS_PROVIDER和NETWORK_PROVIDER两种
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        ZLog.v(ZTag.TAG_DEBUG, bestProvider);
        //
        Location location = locationManager.getLastKnownLocation(bestProvider);
        updateUI(location);
        //
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateUI(location);
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
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {
                    super.onSatelliteStatusChanged(status);
                    updateGpsStatus(StringUtil.format("onSatelliteStatusChanged: %s", status));
                }
            });
        } else {
            locationManager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    String desc = StringUtil.format("onGpsStatusChanged event: %s", event);
                    switch (event) {
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            desc = "第一次定位";
                            break;
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            desc = "卫星状态改变";
                            break;
                        case GpsStatus.GPS_EVENT_STARTED:
                            desc = "定位启动";
                            break;
                        case GpsStatus.GPS_EVENT_STOPPED:
                            desc = "定位结束";
                            break;
                        default:
                            break;
                    }
                    updateGpsStatus(desc);
                }
            });

            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
            //获取默认最大卫星数
            int maxSatellites = gpsStatus.getMaxSatellites();
            //获取第一次定位时间（启动到第一次定位）
            int costTime = gpsStatus.getTimeToFirstFix();
            //获取卫星
            Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
            int count = 0;
            while (iters.hasNext() && count <= maxSatellites) {
                GpsSatellite s = iters.next();
                count++;
            }
//            updateLocation("搜索到卫星:" + count);
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

    /**
     * 方法描述：在View上更新位置信息的显示
     *
     * @param location
     */
    private void updateUI(Location location) {
        double longitude = 0;
        double latitude = 0;
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        view.showLocationInfo(longitude, latitude);

    }

    private void updateGpsStatus(String str) {
        view.showGpsStatus(str);
    }


}
