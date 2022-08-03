package com.zaze.common.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.zaze.common.util.PermissionHelper
import com.zaze.common.widget.dialog.DialogFactory
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:45
 */
abstract class AbsPermissionsActivity : AbsThemeActivity() {

    private val permissions by lazy {
        getPermissionsToRequest()
    }

    private val permissionsRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var permissionGranted = true
            // 是否有权限被永久拒绝，默认false
            var permanentlyDenied = false
            it.forEach { result ->
                ZLog.i(
                    ZTag.TAG,
                    "onRequestPermissionsResult registerForActivityResult: ${result.key}: ${result.value}"
                )
                if (permissionGranted) {
                    permissionGranted = result.value
                }
                // 权限被拒绝 && 不需要解释
                if (!permanentlyDenied && !result.value && !ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        result.key
                    )
                ) {
                    permanentlyDenied = true
                }
            }
            when {
                permissionGranted -> {
                    afterPermissionGranted()
                }
                permanentlyDenied -> {
                    onSomePermanentlyDenied()
                }
                else -> {
                    onPermissionDenied()
                }
            }
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
        return PermissionHelper.hasPermissions(this, permissions)
    }

    open fun setupPermission() {
        lifecycleScope.launchWhenResumed {
            if (hasPermission()) {
                afterPermissionGranted()
            } else {
                beforePermissionGranted()
                permissionsRequest.launch(permissions)
            }
        }
    }

    open fun afterPermissionGranted() {
//        MyLog.i(LcTag.TAG, "afterPermissionGranted")
    }

    open fun beforePermissionGranted() {
//        MyLog.i(LcTag.TAG, "beforePermissionGranted")
    }

    open fun onSomePermanentlyDenied() {
        val builder = DialogFactory.Builder()
            .message("如果没有「${PermissionHelper.getPermissionNames(permissions)}」相关权限，此应用可能无法正常工作。")
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