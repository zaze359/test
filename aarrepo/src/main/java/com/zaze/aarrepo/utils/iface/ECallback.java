package com.zaze.aarrepo.utils.iface;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-06 - 11:16
 */
public abstract class ECallback<D> {

    public void onError(int errorCode, String errorMsg) {
    }

    public void preNext(D d) {
    }

    public abstract void onNext(D d);

    public abstract void onCompleted();

}
