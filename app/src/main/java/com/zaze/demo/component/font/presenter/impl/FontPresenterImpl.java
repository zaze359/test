package com.zaze.demo.component.font.presenter.impl;

import com.zaze.demo.component.font.presenter.FontPresenter;
import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.font.view.FontView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2018-02-07 03:11 1.0
 */
public class FontPresenterImpl extends BaseMvpPresenter<FontView> implements FontPresenter {

    public FontPresenterImpl(FontView view) {
        super(view);
    }

}
