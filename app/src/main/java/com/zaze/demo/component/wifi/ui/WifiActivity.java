package com.zaze.demo.component.wifi.ui;


import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.wifi.adapter.WifiAdapter;
import com.zaze.demo.component.wifi.contract.WifiContract;
import com.zaze.demo.component.wifi.presenter.WifiPresenter;
import com.zaze.utils.ZOnClickHelper;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public class WifiActivity extends BaseActivity implements WifiContract.View {
    RecyclerView wifiRecyclerView;
    Button wifiScanBtn;

    private WifiAdapter adapter;
    private WifiPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_activity);

        wifiRecyclerView = findViewById(R.id.wifi_recycler_view);
        wifiScanBtn = findViewById(R.id.wifi_scan_btn);

        ZOnClickHelper.setOnClickListener(wifiScanBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startScan();
            }
        });
        presenter = new WifiPresenter(this);
        presenter.getNetworkState();
        presenter.startScan();
    }

    @Override
    public void showWifiList(List<ScanResult> list) {
        if (adapter == null) {
            adapter = new WifiAdapter(this, list);
            wifiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            wifiRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
    }
}