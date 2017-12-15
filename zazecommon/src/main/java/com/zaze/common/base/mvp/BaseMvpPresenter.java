package com.zaze.common.base.mvp;

import com.zaze.common.base.BasePresenter;
import com.zaze.common.base.BaseView;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/10/19 - 上午11:22 1.0
 */
public abstract class BaseMvpPresenter<V extends BaseView> implements BasePresenter<V> {
    private V view;

    public BaseMvpPresenter() {
    }

    public BaseMvpPresenter(V view) {
        this.view = view;
    }


    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public boolean isViewAttach() {
        return view != null;
    }
}
