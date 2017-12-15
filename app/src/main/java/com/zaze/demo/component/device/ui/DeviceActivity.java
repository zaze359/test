package com.zaze.demo.component.device.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.common.adapter.third.UltimateRecyclerViewHelper;
import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.device.adapter.DeviceAdapter;
import com.zaze.demo.component.device.presenter.DevicePresenter;
import com.zaze.demo.component.device.presenter.impl.DevicePresenterImpl;
import com.zaze.demo.component.device.view.DeviceView;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.ZStringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DeviceActivity extends BaseActivity implements DeviceView {

    @Bind(R.id.device_screen)
    TextView deviceScreen;
    @Bind(R.id.device_density)
    TextView deviceDensity;
    @Bind(R.id.device_recycler_view)
    UltimateRecyclerView deviceRecyclerView;
    @Bind(R.id.device_mac_address)
    TextView deviceMacAddress;

    private DevicePresenter presenter;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);
        deviceScreen.setText(ZStringUtil.format(
                "屏幕分辨率 : %dx%d",
                ZDisplayUtil.SCREEN_WIDTH_PIXELS,
                ZDisplayUtil.SCREEN_HEIGHT_PIXELS)
        );
        deviceDensity.setText(ZStringUtil.format("屏幕密度 : %s(%d)", ZDisplayUtil.getDensityDpiName(), ZDisplayUtil.SCREEN_DENSITY_DPI));
        presenter = new DevicePresenterImpl(this);
        presenter.getDeviceInfo();
    }

    @Override
    public void showDeviceInfo(List<DeviceStatus> list) {
        if (adapter == null) {
            adapter = new DeviceAdapter(this, list);
            UltimateRecyclerViewHelper.init(deviceRecyclerView);
            deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            deviceRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }

    @Override
    public void showMacAddress(String macAddress) {
        deviceMacAddress.setText(ZStringUtil.parseString(macAddress));
    }
}
