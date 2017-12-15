package com.zaze.demo.component.okhttp.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.okhttp.presenter.OkHttpPresenter;
import com.zaze.demo.component.okhttp.view.OkHttpView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-06-15 01:25 1.0
 */
public class OkHttpPresenterImpl extends BaseMvpPresenter<OkHttpView> implements OkHttpPresenter {

    public OkHttpPresenterImpl(OkHttpView view) {
        super(view);
    }

}
