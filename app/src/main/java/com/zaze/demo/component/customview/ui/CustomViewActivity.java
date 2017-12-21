package com.zaze.demo.component.customview.ui;


import android.os.Bundle;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.customview.presenter.CustomViewPresenter;
import com.zaze.demo.component.customview.presenter.impl.CustomViewPresenterImpl;
import com.zaze.demo.component.customview.view.CustomViewView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-21 10:38 1.0
 */
public class CustomViewActivity extends BaseActivity implements CustomViewView {
    private CustomViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        presenter = new CustomViewPresenterImpl(this);
    }

}