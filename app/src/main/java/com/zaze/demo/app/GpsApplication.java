package com.zaze.demo.app;

import com.zaze.aarrepo.commons.base.ZBaseApplication;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-14 - 13:58
 */
public class GpsApplication extends ZBaseApplication {
//    private LocationClient locationClient = null;
//    private static final CopyOnWriteArrayList<BDLocationListener> locationListenerList = new CopyOnWriteArrayList<>();
//
//    public static GpsApplication getInstance() {
//        return (GpsApplication) ZBaseApplication.getInstance();
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //声明LocationClient类
//        locationClient = new LocationClient(getApplicationContext());
//        //注册监听函数
//        locationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                onReceiveLocations(bdLocation);
//            }
//
//            @Override
//            public void onConnectHotSpotMessage(String s, int i) {
//                onConnectHotSpotMessages(s, i);
//            }
//        });
//        locationClient.setLocOption(getLocationOption());
//    }
//
//    private void onReceiveLocations(BDLocation bdLocation) {
//        for (BDLocationListener listener : locationListenerList) {
//            listener.onReceiveLocation(bdLocation);
//        }
//    }
//
//    private void onConnectHotSpotMessages(String s, int i) {
//        for (BDLocationListener listener : locationListenerList) {
//            listener.onConnectHotSpotMessage(s, i);
//        }
//    }
//
//    protected LocationClientOption getLocationOption() {
//        LocationClientOption option = new LocationClientOption();
//
//        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//
//        //可选，默认gcj02，设置返回的定位结果坐标系
//        option.setCoorType("bd09ll");
//
//        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        int span = 1000;
//        option.setScanSpan(span);
//        //可选，设置是否需要地址信息，默认不需要
//        option.setIsNeedAddress(true);
//
//        //可选，默认false,设置是否使用gps
//        option.setOpenGps(true);
//
//        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setLocationNotify(true);
//
//        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationDescribe(true);
//
//        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIsNeedLocationPoiList(true);
//
//        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setIgnoreKillProcess(false);
//
//        //可选，默认false，设置是否收集CRASH信息，默认收集
//        option.SetIgnoreCacheException(false);
//
//        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//        option.setEnableSimulateGps(false);
//        return option;
//    }
//
//
//    // ---------------------
//    public void registerLocationListener(BDLocationListener locationListener) {
//        locationListenerList.add(locationListener);
//    }
//
//    public void unRegisterLocationListener(BDLocationListener locationListener) {
//        locationListenerList.remove(locationListener);
//    }
//
//    // ---------------------
//    public LocationClient getLocationClient() {
//        return locationClient;
//    }
}
