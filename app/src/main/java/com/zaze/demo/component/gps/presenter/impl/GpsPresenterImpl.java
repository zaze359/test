package com.zaze.demo.component.gps.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.provider.Settings;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.common.util.ActivityUtil;
import com.zaze.demo.app.MyApplication;
import com.zaze.demo.component.gps.LocationHelper;
import com.zaze.demo.component.gps.presenter.GpsPresenter;
import com.zaze.demo.component.gps.view.GpsView;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public class GpsPresenterImpl extends BaseMvpPresenter<GpsView> implements GpsPresenter {

    private LocationManager locationManager;

    public GpsPresenterImpl(GpsView view) {
        super(view);
        locationManager = (LocationManager) MyApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
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
            ActivityUtil.startActivityForResult(getView().getContext(), intent, 1);
            return;
        }
        LocationHelper.start();

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

    @Override
    public void stop() {
        LocationHelper.stop();
    }
}
