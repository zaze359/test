package com.zaze.common.base;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/10/19 - 上午11:22 1.0
 */
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
