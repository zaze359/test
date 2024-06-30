package com.zaze.demo.component.wifi;


import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.widget.Button;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.utils.ext.ClickExtKt;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


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

        ClickExtKt.setSingleClickListener(wifiScanBtn, 1000L, new Function1<Button, Unit>() {
            @Override
            public Unit invoke(Button button) {
                presenter.startScan();
                return Unit.INSTANCE;
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