package com.zaze.demo.feature.communication.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
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
@Deprecated
public class TestBinder extends Binder implements IInterface {
    private static final String DESCRIPTOR = "com.zaze.demo.TestBinder";

    public TestBinder() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}

