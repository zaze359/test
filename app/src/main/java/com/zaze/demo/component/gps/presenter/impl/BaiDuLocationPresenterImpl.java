//package com.zaze.demo.component.gps.presenter.impl;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.Poi;
//import com.zaze.aarrepo.commons.base.ZBasePresenter;
//import com.zaze.aarrepo.commons.log.LogKit;
//import com.zaze.demo.app.GpsApplication;
//import com.zaze.demo.component.gps.presenter.GpsPresenter;
//import com.zaze.demo.component.gps.view.GpsView;
//
//import java.util.List;
//
///**
// * Description :
// *
// * @author : ZAZE
// * @version : 2017-02-14 - 13:30
// */
//public class BaiDuLocationPresenterImpl extends ZBasePresenter<GpsView> implements GpsPresenter {
//    private GpsApplication application;
//    private LocationClient locationClient;
//    private BDLocationListener locationListener;
//
//    public BaiDuLocationPresenterImpl(GpsView view) {
//        super(view);
//        application = GpsApplication.getInstance();
//        locationClient = application.getLocationClient();
//        locationListener = new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation location) {
//                showLocationInfo(location);
//            }
//
//            @Override
//            public void onConnectHotSpotMessage(String s, int i) {
//
//            }
//        };
//    }
//
//
//    @Override
//    public void register() {
//        application.registerLocationListener(locationListener);
//    }
//
//    @Override
//    public void unRegister() {
//        application.unRegisterLocationListener(locationListener);
//    }
//
//    @Override
//    public void start() {
//        locationClient.start();
//    }
//
//    @Override
//    public void stop() {
//        locationClient.stop();
//    }
//
//
//    private void showLocationInfo(BDLocation location) {
//        //获取定位结果
//        StringBuffer sb = new StringBuffer();
//        sb.append("获取定位时间(time) : ");
//        sb.append(location.getTime());    //获取定位时间
//        sb.append("\nerror code : ");
//        sb.append(location.getLocType());    //获取类型类型
//
//        sb.append("\n纬度信息(latitude) : ");
//        sb.append(location.getLatitude());    //获取纬度信息
//
//        sb.append("\n经度信息(longitude) : ");
//        sb.append(location.getLongitude());    //获取经度信息
//
//        sb.append("\n定位精准度(radius) : ");
//        sb.append(location.getRadius());    //获取定位精准度
//
//        if (location.getLocType() == BDLocation.TypeGpsLocation) {
//
//            // GPS定位结果
//            sb.append("\nspeed(单位：公里每小时) : ");
//            sb.append(location.getSpeed());    // 单位：公里每小时
//
//            sb.append("\nsatellite(卫星数) : ");
//            sb.append(location.getSatelliteNumber());    //获取卫星数
//
//            sb.append("\nheight(海拔高度信息，单位米) : ");
//            sb.append(location.getAltitude());    //获取海拔高度信息，单位米
//
//            sb.append("\ndirection(方向信息，单位度) : ");
//            sb.append(location.getDirection());    //获取方向信息，单位度
//
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //获取地址信息
//
//            sb.append("\ndescribe : ");
//            sb.append("gps定位成功");
//
//        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//            // 网络定位结果
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //获取地址信息
//
//            sb.append("\noperationers(运营商信息) : ");
//            sb.append(location.getOperators());    //获取运营商信息
//
//            sb.append("\ndescribe : ");
//            sb.append("网络定位成功");
//
//        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//
//            // 离线定位结果
//            sb.append("\ndescribe : ");
//            sb.append("离线定位成功，离线定位结果也是有效的");
//
//        } else if (location.getLocType() == BDLocation.TypeServerError) {
//
//            sb.append("\ndescribe : ");
//            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//
//        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//
//            sb.append("\ndescribe : ");
//            sb.append("网络不同导致定位失败，请检查网络是否通畅");
//
//        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//            sb.append("\ndescribe : ");
//            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//        }
//
//        sb.append("\nlocationdescribe(位置语义化信息) : ");
//        sb.append(location.getLocationDescribe());    //位置语义化信息
//
//        List<Poi> list = location.getPoiList();    // POI数据
////        名称、类别、经度纬度、附近的酒店饭店商铺
//        if (list != null) {
//            sb.append("\npoilist size = : ");
//            sb.append(list.size());
//            for (Poi p : list) {
//                sb.append("\npoi= : ");
//                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//            }
//        }
//        LogKit.i("BaiduLocationApiem %s", sb.toString());
//        view.showProviderStatus(sb.toString());
//    }
//
//}
