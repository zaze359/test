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
import com.zaze.common.base.SingleLiveEvent
import com.zaze.utils.IntentFactory
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
//        if (!isAdminActive()) {
//            activeAction.set()
//            return
//        }
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
            ZLog.i(
                DeviceAdminActivity.TAG,
                "设备管理器激活状态 $isActive  ${System.currentTimeMillis()}"
            )
            return isActive
        } catch (e: Exception) {
            ZLog.w(DeviceAdminActivity.TAG, "获取设备管理器激活状态 发生异常", e)
            true
        }
    }

    private fun getAdminComponentName(): ComponentName {
        return ComponentName(BaseApplication.getInstance(), MyAdminReceiver::class.java)
    }

    private fun getDevicePolicyManager(): DevicePolicyManager {
        return BaseApplication.getInstance()
            .getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    fun addDeviceAdmin(context: Activity, requestCode: Int): Boolean {
        if (isAdminActive()) {
            return true
        }
        try {
            val intent = IntentFactory.SpecialPermission.addDeviceAdmin(
                getAdminComponentName(),
                "测试设备管理器"
            )
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