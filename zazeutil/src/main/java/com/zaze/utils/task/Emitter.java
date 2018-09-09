package com.zaze.utils.task;

import android.support.annotation.NonNull;

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
    void onExecute(@NonNull T value) throws Exception;


    /**
     * Signal a completion.
     */
    void onComplete();
}