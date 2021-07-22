package com.zaze.common.base

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:45
 */
abstract class AbsPermissionsActivity : AbsThemeActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var permissions: Array<String>

    companion object {
        const val REQUEST_CODE_REQUEST_PERMISSIONS = 1;
        private const val REQUEST_CODE_APP_SETTINGS = 2;
    }

    open fun getPermissionsToRequest(): Array<String> {
        return arrayOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = getPermissionsToRequest()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        if (hasPermission()) {
            afterPermissionGranted()
        } else {
            beforePermissionGranted()
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (hasPermission()) {
            afterPermissionGranted()
        } else {
            beforePermissionGranted()
        }
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        if (hasPermission()) {
            afterPermissionGranted()
        } else {
            beforePermissionGranted()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE_APP_SETTINGS == requestCode) {
            setupPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
//                .setTitle("所需权限")
//                .setRationale("如果没有相关权限，此应用可能无法正常工作。打开应用程序设置以修改应用程序权限")
                .setRequestCode(REQUEST_CODE_APP_SETTINGS)
//                .setNegativeButton("取消")
//                .setPositiveButton("确定")
                .build()
                .show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        ZLog.i(ZTag.TAG, "onPermissionsGranted :$perms")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun hasPermission(): Boolean {
        return permissions.isEmpty() || EasyPermissions.hasPermissions(this, *permissions)
    }

    open fun setupPermission() {
        if (!hasPermission()) {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, REQUEST_CODE_REQUEST_PERMISSIONS, *permissions)
//                .setRationale("123456")
//                .setPositiveButtonText("确定")
//                .setNegativeButtonText("取消")
                    .build()
            )
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_REQUEST_PERMISSIONS)
    open fun afterPermissionGranted() {
    }

    open fun beforePermissionGranted() {

    }
}