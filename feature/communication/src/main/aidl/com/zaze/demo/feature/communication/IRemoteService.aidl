// IRemoteService.aidl
package com.zaze.demo.feature.communication;
import com.zaze.demo.feature.communication.IMessageService;
import android.os.ParcelFileDescriptor;

// Declare any non-default types here with import statements

interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    IMessageService getMessageService();
//    IBinder queryService(String descriptor);

    ParcelFileDescriptor read(String fileName);

    void writeFile(in ParcelFileDescriptor fileDescriptor, String fileName);
}