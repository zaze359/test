package com.zaze.demo.component.device.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.demo.R;
import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.utils.LocalDisplay;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.demo.component.device.adapter.DeviceAdapter;
import com.zaze.demo.component.device.presenter.DevicePresenter;
import com.zaze.demo.component.device.presenter.impl.DevicePresenterImpl;
import com.zaze.demo.component.device.view.DeviceView;
import com.zz.library.util.helper.UltimateRecyclerViewHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DeviceActivity extends ZBaseActivity implements DeviceView {

    @Bind(R.id.device_screen)
    TextView deviceScreen;
    @Bind(R.id.device_density)
    TextView deviceDensity;
    @Bind(R.id.device_recycler_view)
    UltimateRecyclerView deviceRecyclerView;

    private DevicePresenter presenter;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);
        deviceScreen.setText(StringUtil.format(
                "屏幕分辨率 : %dx%d",
                LocalDisplay.SCREEN_WIDTH_PIXELS,
                LocalDisplay.SCREEN_HEIGHT_PIXELS)
        );
        deviceDensity.setText(StringUtil.format("屏幕密度 : %1.2f", LocalDisplay.SCREEN_DENSITY));
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
}
