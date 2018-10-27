package com.zaze.demo.component.gps.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.gps.presenter.GpsPresenter;
import com.zaze.demo.component.gps.presenter.impl.GpsPresenterImpl;
import com.zaze.demo.component.gps.view.GpsView;
import com.zaze.utils.ZStringUtil;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-06 09:59 1.0
 */
public class GpsActivity extends BaseActivity implements GpsView {
    TextView gpsStatusTv;
    TextView gpsLocationTv;
    TextView gpsAddressTv;
    private GpsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_activity);
        gpsStatusTv = findViewById(R.id.gps_status_tv);
        gpsLocationTv = findViewById(R.id.gps_location_tv);
        gpsAddressTv = findViewById(R.id.gps_address_tv);


        presenter = new GpsPresenterImpl(this);
//        presenter = new BaiDuLocationPresenterImpl(this);
        presenter.register();
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
        presenter.unRegister();
    }

    @Override
    public void showLocationInfo(double longitude, double latitude) {
        gpsLocationTv.setText(ZStringUtil.parseString("当前经度：" + longitude + "\n当前纬度：" + latitude));
    }

    @Override
    public void showProviderStatus(String str) {
        gpsStatusTv.setText(ZStringUtil.parseString(str));
    }

    @Override
    public void showAddress(String str) {
        gpsAddressTv.setText(ZStringUtil.parseString(str));
    }
}
