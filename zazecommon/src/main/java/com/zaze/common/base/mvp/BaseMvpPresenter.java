package com.zaze.common.base.mvp;

import com.zaze.common.base.BaseView;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/10/19 - 上午11:22 1.0
 */
public abstract class BaseMvpPresenter<V extends BaseView> {
    private V view;

    public BaseMvpPresenter() {
    }

    public BaseMvpPresenter(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    protected boolean isViewAttach() {
        return getView() != null;
    }

}
