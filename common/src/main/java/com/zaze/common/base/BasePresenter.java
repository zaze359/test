package com.zaze.common.base;

/**
 * Description :
 *
 * @author zaze
 * @version 2017/10/19 - 上午11:22 1.0
 */
@Deprecated
public interface BasePresenter<V extends BaseView> {

    V getView();

    void attachView(V view);

    void detachView();

    boolean isViewAttach();

}
