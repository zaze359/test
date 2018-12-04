package com.zaze.demo.component.device.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.device.adapter.DeviceAdapter;
import com.zaze.demo.component.device.contract.DeviceContract;
import com.zaze.demo.component.device.presenter.DevicePresenter;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DeviceActivity extends BaseActivity implements DeviceContract.View {

    TextView deviceScreen;
    TextView deviceDensity;
    TextView deviceDpi;

    RecyclerView deviceRecyclerView;
    TextView deviceMacAddress;
    EditText deviceInput;

    private DevicePresenter presenter;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);
        deviceScreen = findViewById(R.id.device_screen);
        deviceDensity = findViewById(R.id.device_density);
        deviceRecyclerView = findViewById(R.id.device_recycler_view);
        deviceMacAddress = findViewById(R.id.device_mac_address);
        deviceInput = findViewById(R.id.device_input);
        deviceDpi = findViewById(R.id.device_dpi);
        deviceScreen.setText(ZStringUtil.format(
                "屏幕分辨率 : %dx%d(%1.0fx%1.0f)",
                ZDisplayUtil.getScreenWidthPixels(),
                ZDisplayUtil.getScreenHeightPixels(),
                ZDisplayUtil.getScreenWidthDp(),
                ZDisplayUtil.getScreenHeightDp())

        );
        findViewById(R.id.device_calculate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.calculate(deviceInput.getText().toString());
            }
        });
        deviceDensity.setText(ZStringUtil.format("屏幕密度 : %s(%d)(%s)", ZDisplayUtil.getDensityDpiName(), ZDisplayUtil.getScreenDensityDpi(), ZDisplayUtil.getScreenDensity()));
        presenter = new DevicePresenter(this);
        presenter.getDeviceInfo();
    }

    @Override
    public void showMacAddress(@NotNull String macAddress) {
        deviceMacAddress.setText(macAddress);
    }

    @Override
    public void showDeviceInfo(@NotNull ArrayList<DeviceStatus> list) {
        if (adapter == null) {
            adapter = new DeviceAdapter(this, list);
            deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            deviceRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }

    @Override
    public void showDpi(@NotNull String dpi) {
        deviceDpi.setText(dpi);
    }
}
