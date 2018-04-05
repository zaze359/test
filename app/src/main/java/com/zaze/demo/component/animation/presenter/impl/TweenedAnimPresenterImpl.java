package com.zaze.demo.component.animation.presenter.impl;

import com.zaze.demo.component.animation.presenter.TweenedAnimPresenter;
import com.zaze.demo.component.animation.view.TweenedAnimView;
import com.zaze.common.base.mvp.BaseMvpPresenter;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2018-04-05 12:07 1.0
 */
public class TweenedAnimPresenterImpl extends BaseMvpPresenter<TweenedAnimView> implements TweenedAnimPresenter {
    public TweenedAnimPresenterImpl(TweenedAnimView view) {
        super(view);
    }
}
