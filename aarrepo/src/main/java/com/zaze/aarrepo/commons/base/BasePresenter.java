package com.zaze.aarrepo.commons.base;

public abstract class BasePresenter<V extends BaseView> {

    public V view;

    public BasePresenter(V view) {
        this.view = view;
    }

    public String getString(int resId) {
        return BaseApplication.getInstance().getString(resId);
    }

    public String getString(int resId, Object... objs) {
        return BaseApplication.getInstance().getString(resId, objs);
    }
}
