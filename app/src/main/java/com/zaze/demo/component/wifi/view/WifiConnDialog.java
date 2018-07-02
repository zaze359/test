package com.zaze.demo.component.wifi.view;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.zaze.demo.R;
import com.zaze.utils.ZOnClickHelper;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-06-15 - 14:47
 */
public class WifiConnDialog extends Dialog {

    private Button wifiDialogConnBtn;

    private ScanResult scanResult;

    public WifiConnDialog(@NonNull Context context) {
        super(context);
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_dialog_conn);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        wifiDialogConnBtn = (Button) findViewById(R.id.wifi_dialog_conn_btn);
        ZOnClickHelper.setOnClickListener(wifiDialogConnBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
