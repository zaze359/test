package com.zaze.demo.feature.anim.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.feature.anim.presenter.TransitionPresenter;
import com.zaze.demo.feature.anim.view.TransitionView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:33 1.0
 */
public class TransitionPresenterImpl extends BaseMvpPresenter<TransitionView> implements TransitionPresenter {

    public TransitionPresenterImpl(TransitionView view) {
        super(view);
    }

}
