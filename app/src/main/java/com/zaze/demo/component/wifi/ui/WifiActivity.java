package com.zaze.demo.component.wifi.ui;


import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.wifi.adapter.WifiAdapter;
import com.zaze.demo.component.wifi.presenter.WifiPresenter;
import com.zaze.demo.component.wifi.presenter.impl.WifiPresenterImpl;
import com.zaze.demo.component.wifi.view.WifiView;
import com.zaze.utils.ZOnClickHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-25 07:33 1.0
 */
public class WifiActivity extends BaseActivity implements WifiView {
    @Bind(R.id.wifi_recycler_view)
    RecyclerView wifiRecyclerView;
    @Bind(R.id.wifi_scan_btn)
    Button wifiScanBtn;

    private WifiAdapter adapter;
    private WifiPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_activity);
        ButterKnife.bind(this);
        ZOnClickHelper.setOnClickListener(wifiScanBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startScan();
            }
        });
        presenter = new WifiPresenterImpl(this);
        presenter.getNetworkState();
        presenter.startScan();
        presenter.getScanResults();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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