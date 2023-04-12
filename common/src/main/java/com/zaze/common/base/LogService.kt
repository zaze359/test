package com.zaze.common.base

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.FileDescriptor
import java.io.PrintWriter

open class LogService : Service() {
    open val showLifecycle = false

    private val name by lazy {
        this.javaClass.simpleName
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onCreate() {
        super.onCreate()
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onCreate: 服务创建")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onStartCommand: 接收到服务启动指令")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onDestroy: 服务销毁")
    }

    override fun onBind(intent: Intent?): IBinder? {
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onBind: 第一次绑定服务")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onBind: 所有客户端已解绑")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        if (showLifecycle) ZLog.i(ZTag.TAG_DEBUG, "$name onBind: 多个客户端绑定")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        super.dump(fd, writer, args)
    }
}