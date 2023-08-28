package com.zaze.demo.feature.communication.aidl

import android.os.Build
import com.zaze.demo.feature.communication.IMessageService
import com.zaze.demo.feature.communication.parcel.IpcMessage

class MessageService private constructor(): IMessageService.Stub() {
    companion object {
        val instance: MessageService
            get() = _MessageService.instance
    }

    private var count = 0
    override fun getMessage(): IpcMessage {
        count++
        val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            "aidl message $count; ${android.os.Process.myProcessName()}(${android.os.Process.myPid()})"
        } else {
            "aidl message $count; pid=${android.os.Process.myPid()}"
        }
        return IpcMessage().apply {
            this.id = count
            this.data = message
        }
    }
    private object _MessageService {
        val instance = MessageService()
    }

}