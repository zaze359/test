package com.zaze.common.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.permission.PermissionRequest
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

abstract class AbsPermissionFragment : AbsViewModelFragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    val permissionRequest = PermissionRequest(this)

    open fun getPermissionsToRequest(): Array<String> {
        return arrayOf()
    }

    open fun setupPermission() {
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