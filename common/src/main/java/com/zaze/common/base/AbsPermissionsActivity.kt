package com.zaze.common.base

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.permission.PermissionRequest
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:45
 */
abstract class AbsPermissionsActivity : AbsThemeActivity() {

    val permissionRequest = PermissionRequest(this)

    open fun getPermissionsToRequest(): Array<String> {
        return arrayOf()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupPermission()
    }

    fun setupPermission() {
        val permissions = getPermissionsToRequest()
        if (permissions.isEmpty()) {
            return
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                permissionRequest.beforePermissionGranted {
                    beforePermissionGranted()
                }.onPermissionGranted {
                    afterPermissionGranted()
                }.request(getPermissionsToRequest())
            }
        }
    }

    /**
     * 获取权限后
     */
    open fun afterPermissionGranted() {
        ZLog.i(ZTag.TAG, "afterPermissionGranted")
    }

    /**
     * 获取权限前
     */
    open fun beforePermissionGranted() {
        ZLog.i(ZTag.TAG, "beforePermissionGranted")
    }
}