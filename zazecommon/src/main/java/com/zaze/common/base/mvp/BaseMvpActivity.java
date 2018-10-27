package com.zaze.common.base.mvp;


import android.os.Bundle;

import com.zaze.common.base.BaseActivity;
import com.zaze.common.base.BasePresenter;
import com.zaze.common.base.BaseView;

import androidx.annotation.Nullable;


/**
 * Description : 基于mvp 对 BaseActivity 进行再次封装
 *
 * @author : zaze
 * @version : 2017-02-06 22:59 2.0
 */
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        presenter.attachView((V) this);
    }

    /**
     * Presenter
     *
     * @return Presenter
     */
    protected abstract P getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
