package com.zaze.aarrepo.commons.base;

public abstract class ZBasePresenter<V extends ZBaseView> {

    public V view;

    public ZBasePresenter(V view) {
        this.view = view;
    }

    public String getString(int resId) {
        return ZBaseApplication.getInstance().getString(resId);
    }

    public String getString(int resId, Object... objs) {
        return ZBaseApplication.getInstance().getString(resId, objs);
    }
}
