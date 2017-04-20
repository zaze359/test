package com.zaze.demo.component.provider.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.utils.helper.OnClickHelper;
import com.zaze.demo.R;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.presenter.impl.ProviderPresenterImpl;
import com.zaze.demo.component.provider.view.ProviderView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderActivity extends ZBaseActivity implements ProviderView {
    @Bind(R.id.insert_btn)
    Button insertBtn;
    @Bind(R.id.query_btn)
    Button queryBtn;
    private ProviderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        ButterKnife.bind(this);
        presenter = new ProviderPresenterImpl(this);
        OnClickHelper.setOnClickListener(insertBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.insert();
            }
        });
        OnClickHelper.setOnClickListener(queryBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.query();
            }
        });
    }
}
