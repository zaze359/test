package com.zaze.common.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.widget.dialog.DialogFactory
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

/**
 * must create before activity are STARTED.
 */
class ZPermission(private val activity: FragmentActivity) {

    private var permissions: Array<String> = emptyArray()

    private val permissionsRequest =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissionHandler.onActivityResult(it)
        }

    private val startSettingRequest =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (hasPermission()) {
                afterPermissionGranted()
            } else {
                setupPermission()
            }
        }

    private val permissionHandler by lazy {
        PermissionHandler(
            activity = activity,
            permissions = getPermissionsToRequest(),
            afterPermissionGranted = ::afterPermissionGranted,
            onSomePermanentlyDenied = ::onSomePermanentlyDenied,
            onPermissionDenied = ::onPermissionDenied
        )
    }

    private fun getPermissionsToRequest(): Array<String> {
        return this.permissions
    }

    private fun afterPermissionGranted() {
        ZLog.i(ZTag.TAG, "afterPermissionGranted")
    }

    /**
     * 获取权限前
     */
    private fun beforePermissionGranted() {
        ZLog.i(ZTag.TAG, "beforePermissionGranted")
    }

    /**
     * 部分权限被拒绝
     */
    private fun onSomePermanentlyDenied() {
        val builder = DialogFactory.Builder()
            .message("如果没有「${permissionHandler.getDeniedPermissionNames()}」相关权限，此应用可能无法正常工作。")
            .negative("取消") {
                activity.finish()
            }
        builder.positive {
            ZLog.i(ZTag.TAG, "openApplicationDetailsSetting")
            // 打开设置
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                Uri.fromParts("package", activity.packageName, null)
            )
            startSettingRequest.launch(intent)
        }
        builder.build().show(activity.supportFragmentManager)
    }

    private fun onPermissionDenied() {
        setupPermission()
    }

    private fun hasPermission(): Boolean {
        return permissionHandler.hasPermissions()
    }

    private fun setupPermission() {
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (hasPermission()) {
                    afterPermissionGranted()
                } else {
                    beforePermissionGranted()
                    permissionHandler.launch(permissionsRequest)
                }
            }
        }
    }

    fun request(permissions: Array<String>) {
        this.permissions = permissions
        permissionHandler.launch(permissionsRequest)
    }

}