package com.zaze.demo.component.animation.ui;

import android.os.Bundle;

import com.zaze.common.base.mvp.BaseMvpActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.presenter.TweenedAnimPresenter;
import com.zaze.demo.component.animation.presenter.impl.TweenedAnimPresenterImpl;
import com.zaze.demo.component.animation.view.TweenedAnimView;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2018-04-05 12:07 1.0
 */
public class TweenedAnimActivity extends BaseMvpActivity<TweenedAnimView, TweenedAnimPresenter> implements TweenedAnimView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweened_anim);
    }

    @Override
    protected TweenedAnimPresenter getPresenter() {
        return new TweenedAnimPresenterImpl(this);
    }

}