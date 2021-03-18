package com.zaze.demo.component.device;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.base.AbsActivity;
import com.zaze.common.base.ext.ViewModelFactoryKt;
import com.zaze.demo.R;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.demo.theme.ThemeUtils;
import com.zaze.demo.theme.widgets.TintConstraintLayout;
import com.zaze.demo.theme.widgets.TintImageView;
import com.zaze.utils.DisplayUtil;
import com.zaze.utils.NetUtil;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DeviceActivity extends AbsActivity {

    TextView deviceScreen;
    TextView deviceDensity;
    TextView deviceInch;

    RecyclerView deviceRecyclerView;
    TextView deviceMacAddress;
    EditText deviceInput;

    private DeviceAdapter adapter;
    private DeviceViewModel viewModel;

    private TintImageView device_test_tint_iv;
    private TintConstraintLayout device_test_tint_layout;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.device_activity);
        deviceScreen = findViewById(R.id.device_screen);
        deviceDensity = findViewById(R.id.device_density);
        deviceRecyclerView = findViewById(R.id.device_recycler_view);
        deviceMacAddress = findViewById(R.id.device_mac_address);
        deviceInput = findViewById(R.id.device_input);
        deviceInch = findViewById(R.id.device_inch);
        device_test_tint_iv = findViewById(R.id.device_test_tint_iv);
        device_test_tint_layout = findViewById(R.id.device_test_tint_layout);
        viewModel = ViewModelFactoryKt.obtainViewModel(this, DeviceViewModel.class);
        viewModel.deviceInfoList.observe(this, new Observer<ArrayList<DeviceStatus>>() {
            @Override
            public void onChanged(ArrayList<DeviceStatus> deviceStatuses) {
                showDeviceInfo(deviceStatuses);
            }
        });
        viewModel.inchData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                deviceInch.setText(s);
            }
        });
        viewModel.loadDeviceInfo();
        //
        findViewById(R.id.device_calculate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(R.style.BlackTheme);
                device_test_tint_iv.setImageResource(R.mipmap.ic_looks_2);
                viewModel.calculatePhysicalSize(0);
            }
        });
        showBaseInfo();
    }

    private void showBaseInfo() {
        deviceScreen.setText(ZStringUtil.format(
                "屏幕分辨率 : %dx%d",
                DisplayUtil.getMetrics().widthPixels,
                DisplayUtil.getMetrics().heightPixels)
        );
        deviceDensity.setText(ZStringUtil.format("屏幕密度 : %s(%d)(%s)", DisplayUtil.getDensityDpiName(), DisplayUtil.getMetrics().densityDpi, DisplayUtil.getMetrics().density));
        deviceMacAddress.setText("WLAN MAC地址 : " + NetUtil.getWLANMacAddress());
    }

    private void showDeviceInfo(@NotNull ArrayList<DeviceStatus> list) {
        if (adapter == null) {
            adapter = new DeviceAdapter(this, list);
            deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            deviceRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }

    @Override
    public void setTheme(int resId) {
        super.setTheme(resId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ThemeUtils.INSTANCE.refreshUI(this);
        }
    }
}
