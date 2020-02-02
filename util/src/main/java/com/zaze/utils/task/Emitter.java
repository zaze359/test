package com.zaze.utils.task;


public interface Emitter<T> {

    /**
     * Signal a Throwable exception.
     *
     * @param error the Throwable to signal, not null
     */
    void onError(Throwable error);

    /**
     * Signal a normal value.
     *
     * @param value the value to signal, not null
     */
    void onExecute(T value) throws Exception;


    /**
     * Signal a completion.
     */
    void onComplete();
}