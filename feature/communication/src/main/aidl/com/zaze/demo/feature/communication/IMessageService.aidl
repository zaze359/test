// IMessageService.aidl
package com.zaze.demo.feature.communication;
import com.zaze.demo.feature.communication.parcel.IpcMessage;

// Declare any non-default types here with import statements

interface IMessageService {
    const String DESCRIPTOR = "com.zaze.demo.feature.communication.IMessageService";
    IpcMessage getMessage();
}