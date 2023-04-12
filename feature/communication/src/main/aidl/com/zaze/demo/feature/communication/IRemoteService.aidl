// IRemoteService.aidl
package com.zaze.demo.feature.communication;
import com.zaze.demo.feature.communication.parcel.IpcMessage;
// Declare any non-default types here with import statements

interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    IpcMessage getMessage();
}