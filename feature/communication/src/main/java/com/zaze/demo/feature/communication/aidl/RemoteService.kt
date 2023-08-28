package com.zaze.demo.feature.communication.aidl

import android.content.Intent
import android.os.IBinder
import android.os.ParcelFileDescriptor
import com.zaze.common.base.LogService
import com.zaze.common.thread.ThreadPlugins
import com.zaze.demo.feature.communication.IMessageService
import com.zaze.demo.feature.communication.IRemoteService
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 这里 aidl的服务端，实现了对应的服务功能
 */
class RemoteService : LogService() {

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

        override fun getMessageService(): IMessageService {
            return MessageService.instance
        }

        override fun queryService(descriptor: String?): IBinder? {
            return when (descriptor) {
                IMessageService.DESCRIPTOR -> {
                    MessageService.instance
                }

                else -> {
                    null
                }
            }
        }

        override fun read(fileName: String?): ParcelFileDescriptor? {
            if (fileName.isNullOrEmpty()) return null
            // 返回一个数组，表示 输入输出流
            val file = File("/data/data/com.zaze.demo/files/shared/$fileName")
            val pipe = ParcelFileDescriptor.createPipe()
            val write = ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])
            ThreadPlugins.runInIoThread(Runnable {
                FileUtil.write(
                    FileInputStream(file),
                    write
                )
            })
            return pipe[0]
//            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        }

        override fun writeFile(fileDescriptor: ParcelFileDescriptor?, fileName: String?) {
            fileDescriptor ?: return
            if (fileName.isNullOrEmpty()) return
            ZLog.i(ZTag.TAG_DEBUG, "RemoteService: $fileName")
            ThreadPlugins.runInIoThread(Runnable {
                val read = ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor)
                val file = File("/data/data/com.zaze.demo/files/shared/$fileName")
                FileUtil.createFileNotExists(file)
                val outputStream =
                    FileOutputStream(file)
                FileUtil.write(read, outputStream)
            })
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        return binder
    }
}