package com.zaze.utils;

/**
 * Description : 回调接口（）
 *
 * @author : ZAZE
 * @version : 2017-01-06 - 11:16
 */
public abstract class ZCallback<D> {

    public void onError(int errorCode, String errorMsg) {
    }

    public D preNext(D d) {
        return d;
    }

    public abstract void onNext(D d);

    public abstract void onCompleted();

}
