package com.zaze.demo.feature.communication.aidl

import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.zaze.common.base.LogService
import com.zaze.demo.feature.communication.IRemoteService
import com.zaze.demo.feature.communication.parcel.IpcMessage

/**
 * 这里 aidl的服务端，实现了对应的服务功能
 */
class RemoteService : LogService() {
    private var count = 0

    /** 匿名内部类，实现IRemoteService接口 **/
    private val binder = object : IRemoteService.Stub() {

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
            // Does nothing
        }

        override fun getMessage(): IpcMessage {
            count++
            val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                "aidl message $count; ${android.os.Process.myProcessName()}(${android.os.Process.myPid()})"
            } else {
                "aidl message $count; pid=${android.os.Process.myPid()}"
            }
            return IpcMessage().apply {
                this.id = count
                this.message = message
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }
}