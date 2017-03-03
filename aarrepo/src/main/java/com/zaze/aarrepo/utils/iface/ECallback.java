package com.zaze.aarrepo.utils.iface;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-06 - 11:16
 */
public interface ECallback<E> {

    void onError(int errorCode, String errorMsg);

    void onNext(E e);

    void onCompleted();

}
