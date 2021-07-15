package com.zaze.demo.component.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 11:15
 */
class MyAdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.i("MyAdminReceiver", "MyAdminReceiver onEnable")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.w("MyAdminReceiver", "MyAdminReceiver onDisabled")
    }
}