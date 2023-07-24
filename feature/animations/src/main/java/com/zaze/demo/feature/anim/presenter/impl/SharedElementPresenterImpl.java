package com.zaze.demo.feature.anim.presenter.impl;


import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.feature.anim.presenter.SharedElementPresenter;
import com.zaze.demo.feature.anim.view.SharedElementView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:46 1.0
 */
public class SharedElementPresenterImpl extends BaseMvpPresenter<SharedElementView> implements SharedElementPresenter {

    public SharedElementPresenterImpl(SharedElementView view) {
        super(view);
    }

}
