package com.zaze.demo.component.gps.ui;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.zaze.demo.R;
import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.demo.component.gps.presenter.GpsPresenter;
import com.zaze.demo.component.gps.presenter.impl.BaiDuLocationPresenterImpl;
import com.zaze.demo.component.gps.view.GpsView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public class GpsActivity extends ZBaseActivity implements GpsView {
    @Bind(R.id.gps_status_tv)
    TextView gpsStatusTv;
    @Bind(R.id.gps_location_tv)
    TextView gpsLocationTv;
    private GpsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        ButterKnife.bind(this);
//        presenter = new GpsPresenterImpl(this);
        presenter = new BaiDuLocationPresenterImpl(this);
        presenter.register();
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unRegister();
    }

    @Override
    public LocationManager getLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void showLocationInfo(double longitude, double latitude) {
        gpsStatusTv.setText(StringUtil.parseString("当前经度：" + longitude + "\n当前纬度：" + latitude));
    }

    @Override
    public void showGpsStatus(String str) {
        gpsStatusTv.setText(StringUtil.parseString(str));
    }


}
