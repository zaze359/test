package com.zaze.common.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.zaze.common.util.PermissionHandler
import com.zaze.common.widget.dialog.DialogFactory
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:45
 */
abstract class AbsPermissionsActivity : AbsThemeActivity() {

    val permissionHandler by lazy {
        PermissionHandler(
            activity = this,
            permissions = getPermissionsToRequest(),
            afterPermissionGranted = ::afterPermissionGranted,
            onSomePermanentlyDenied = ::onSomePermanentlyDenied,
            onPermissionDenied = ::onPermissionDenied
        )
    }

//    private val permissions by lazy {
//        getPermissionsToRequest()
//    }

//    open fun needExternalStoragePermission(): Boolean{
//        if(permissions.isEmpty()) {
//            return false
//        }
//        val list = permissions.toList()
//        if(permissions.isNotEmpty() permissions.contains()
//    }

    private val permissionsRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissionHandler.onActivityResult(it)
        }

    private val startSettingRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (hasPermission()) {
                afterPermissionGranted()
            } else {
                setupPermission()
            }
        }

    open fun getPermissionsToRequest(): Array<String> {
        return arrayOf()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupPermission()
    }

    private fun hasPermission(): Boolean {
        return permissionHandler.hasPermissions()
    }

    open fun setupPermission() {
        lifecycleScope.launchWhenResumed {
            if (hasPermission()) {
                afterPermissionGranted()
            } else {
                beforePermissionGranted()
                permissionHandler.launch(permissionsRequest)
            }
        }
    }

    /**
     * 获取权限后
     */
    open fun afterPermissionGranted() {
//        MyLog.i(LcTag.TAG, "afterPermissionGranted")
    }

    /**
     * 获取权限前
     */
    open fun beforePermissionGranted() {
//        MyLog.i(LcTag.TAG, "beforePermissionGranted")
    }

    /**
     * 部分权限被拒绝
     */
    open fun onSomePermanentlyDenied() {
        val builder = DialogFactory.Builder()
            .message("如果没有「${permissionHandler.getDeniedPermissionNames()}」相关权限，此应用可能无法正常工作。")
            .negative("取消") {
                finish()
            }
        builder.positive {
            ZLog.i(ZTag.TAG, "openApplicationDetailsSetting")
            // 打开设置
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                Uri.fromParts("package", packageName, null)
            )
            startSettingRequest.launch(intent)
        }
        builder.build().show(supportFragmentManager)
    }

    open fun onPermissionDenied() {
        setupPermission()
    }
}