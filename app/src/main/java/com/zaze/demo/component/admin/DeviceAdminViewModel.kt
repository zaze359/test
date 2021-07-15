package com.zaze.demo.component.admin

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.BaseApplication
import com.zaze.common.base.ext.SingleLiveEvent
import com.zaze.common.base.ext.set
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-02 - 15:23
 */
class DeviceAdminViewModel : AbsViewModel() {

    var itemsData = MutableLiveData<List<AdminItem>>()
    var activeAction = SingleLiveEvent<Unit>()

    fun loadItems() {
        if (!isAdminActive()) {
            activeAction.set()
            return
        }
        val devicePolicyManager = getDevicePolicyManager()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            devicePolicyManager.getWifiMacAddress(getAdminComponentName())
        }
        val list = ArrayList<AdminItem>()
    }

    fun isAdminActive(): Boolean {
        return try {
            val isActive =
                getDevicePolicyManager().isAdminActive(getAdminComponentName())
            ZLog.i(DeviceAdminActivity.TAG, "设备管理器激活状态 $isActive  ${System.currentTimeMillis()}")
            return isActive
        } catch (e: Exception) {
            ZLog.w(DeviceAdminActivity.TAG, "获取设备管理器激活状态 发生异常", e)
            true
        }
    }

    fun getAdminComponentName(): ComponentName {
        return ComponentName(BaseApplication.getInstance(), MyAdminReceiver::class.java)
    }

    fun getDevicePolicyManager(): DevicePolicyManager {
        return BaseApplication.getInstance()
            .getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    fun addDeviceAdmin(context: Activity, requestCode: Int): Boolean {
        try {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, getAdminComponentName())
            // 提示文本
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "test txt")
            context.startActivityForResult(intent, requestCode)
            ZLog.i(DeviceAdminActivity.TAG, "请求启动设备管理器...")
        } catch (e: Exception) {
            ZLog.w(DeviceAdminActivity.TAG, "启动设备管理器 发生异常", e)
            return false
        }
        return true
    }

    fun removeDeviceAdmin() {
        try {
            ZLog.i(DeviceAdminActivity.TAG, "请求解锁设备管理器")
            getDevicePolicyManager().removeActiveAdmin(getAdminComponentName())
        } catch (e: Exception) {
            ZLog.w(DeviceAdminActivity.TAG, "解锁设备管理器 发生异常!", e)
        }
    }
}