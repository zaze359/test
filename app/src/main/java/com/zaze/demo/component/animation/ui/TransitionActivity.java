package com.zaze.demo.component.animation.ui;


import android.os.Bundle;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.presenter.TransitionPresenter;
import com.zaze.demo.component.animation.presenter.impl.TransitionPresenterImpl;
import com.zaze.demo.component.animation.view.TransitionView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:33 1.0
 */
public class TransitionActivity extends BaseActivity implements TransitionView {
    private TransitionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_activity);
        presenter = new TransitionPresenterImpl(this);
    }
}