package com.zaze.demo.component.customview.presenter.impl;

import com.zaze.demo.component.customview.presenter.CustomViewPresenter;
import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.customview.view.CustomViewView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-21 10:38 1.0
 */
public class CustomViewPresenterImpl extends BaseMvpPresenter<CustomViewView> implements CustomViewPresenter {

    public CustomViewPresenterImpl(CustomViewView view) {
        super(view);
    }

}
