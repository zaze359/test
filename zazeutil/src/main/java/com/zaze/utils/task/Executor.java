package com.zaze.utils.task;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-01 - 10:30
 */
public interface Executor<T> {
    void onStart();

    void onExecute(T task) throws Exception;

    void onError();

    void onComplete();
}
