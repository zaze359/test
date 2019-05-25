package com.zaze.demo.binder;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-05-24 - 13:40
 */
public class TestBinder extends Binder {

    public TestBinder() {
//        attachInterface();
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        return super.onTransact(code, data, reply, flags);
    }
}
