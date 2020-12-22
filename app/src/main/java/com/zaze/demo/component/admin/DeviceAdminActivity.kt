package com.zaze.demo.component.admin

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.demo.R
import com.zaze.utils.log.ZLog
import kotlinx.android.synthetic.main.device_admin_act.*

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 14:12
 */
class DeviceAdminActivity : AbsActivity() {
    companion object {
        const val TAG = "DeviceAdminActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_admin_act)
        addDeviceAdminBtn.setOnClickListener {
            addDeviceAdmin(this, 0)
        }
        removeDeviceAdminBtn.setOnClickListener {
            removeDeviceAdmin(this)
        }

        deviceAdminLayout.layoutManager = LinearLayoutManager(this)
        deviceAdminLayout.adapter = DeviceAdminAdapter(this, createItems())
    }


    fun createItems(): List<AdminItem> {
        val devicePolicyManager = getDevicePolicyManager(this)
        val adminComponentName = getAdminComponentName(this)
        val list = ArrayList<AdminItem>()
        return list
    }

    fun removeDeviceAdmin(context: Context) {
        try {
            ZLog.i(TAG, "请求解锁设备管理器")
            getDevicePolicyManager(context).removeActiveAdmin(getAdminComponentName(context))
        } catch (e: Exception) {
            ZLog.w(TAG, "解锁设备管理器 发生异常!", e)
        }
    }

    fun addDeviceAdmin(context: Activity, requestCode: Int): Boolean {
        try {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, getAdminComponentName(context))
            // 提示文本
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "test txt")
            context.startActivityForResult(intent, requestCode)
            ZLog.i(TAG, "请求启动设备管理器...")
        } catch (e: Exception) {
            ZLog.w(TAG, "启动设备管理器 发生异常", e)
            return false
        }
        return true
    }

    fun isAdminActive(context: Context): Boolean {
        return try {
            val isActive = getDevicePolicyManager(context).isAdminActive(getAdminComponentName(context))
            ZLog.i(TAG, "设备管理器激活状态 $isActive  ${System.currentTimeMillis()}")
            return isActive
        } catch (e: Exception) {
            ZLog.w(TAG, "获取设备管理器激活状态 发生异常", e)
            true
        }
    }

    private fun getAdminComponentName(context: Context): ComponentName {
        return ComponentName(context, MyAdminReceiver::class.java)
    }

    private fun getDevicePolicyManager(context: Context): DevicePolicyManager {
        return context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }
}