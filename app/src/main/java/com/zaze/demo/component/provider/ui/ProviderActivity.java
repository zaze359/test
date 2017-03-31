package com.zaze.demo.component.provider.ui;


import android.os.Bundle;

import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.presenter.impl.ProviderPresenterImpl;
import com.zaze.demo.component.provider.view.ProviderView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderActivity extends ZBaseActivity implements ProviderView {
    private ProviderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        presenter = new ProviderPresenterImpl(this);

    }
}
