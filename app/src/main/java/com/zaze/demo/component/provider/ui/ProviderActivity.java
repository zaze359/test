package com.zaze.demo.component.provider.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.presenter.impl.ProviderPresenterImpl;
import com.zaze.demo.component.provider.view.ProviderView;
import com.zaze.utils.ZOnClickHelper;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderActivity extends BaseActivity implements ProviderView {
    Button insertBtn;
    Button queryBtn;
    private ProviderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_activity);

        insertBtn = findViewById(R.id.insert_btn);
        queryBtn = findViewById(R.id.query_btn);
        presenter = new ProviderPresenterImpl(this);
        ZOnClickHelper.setOnClickListener(insertBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.insert();
            }
        });
        ZOnClickHelper.setOnClickListener(queryBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.query();
            }
        });
    }
}
