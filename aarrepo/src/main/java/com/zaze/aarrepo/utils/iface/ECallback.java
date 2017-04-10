package com.zaze.aarrepo.utils.iface;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-06 - 11:16
 */
public abstract class ECallback<E> {

    public abstract void onError(int errorCode, String errorMsg);

    public abstract void preNext(E e);

    public abstract void onNext(E e);

    public abstract void onCompleted();

}
