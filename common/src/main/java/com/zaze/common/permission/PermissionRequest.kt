package com.zaze.common.permission

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.zaze.common.widget.dialog.DialogProvider
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.permission.PermissionCallback
import com.zaze.utils.permission.PermissionHandler

/**
 * 权限请求
 * 需要在 Activity STARTED 前；或者 Fragment created 前。
 */
class PermissionRequest {
    private val permissionsRequest: ActivityResultLauncher<Array<String>>
    private val startSettingRequest: ActivityResultLauncher<Intent>

    private var onPermissionGranted: PermissionCallback? = null
    private var beforePermissionGranted: PermissionCallback? = null
//    private var onSomePermanentlyDenied: PermissionCallback? = null
//    private var onPermissionDenied: PermissionCallback? = null

    private val getFragmentManager: () -> FragmentManager
    private val getViewLifecycleOwner: () -> LifecycleOwner
    private lateinit var getActivity: () -> Activity


    private val permissionHandler by lazy {
        PermissionHandler(
            activity = getActivity(),
            permissions = emptyArray(),
            afterPermissionGranted = ::afterPermissionGranted,
            onSomePermanentlyDenied = ::onSomePermanentlyDenied,
            onPermissionDenied = ::onPermissionDenied
        )
    }

    constructor(activity: FragmentActivity) {
        permissionsRequest =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                ::onPermissionResult
            )
        startSettingRequest =
            activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ::onSettingResult
            )
        getActivity = {
            activity
        }
        getFragmentManager = {
            activity.supportFragmentManager
        }
        getViewLifecycleOwner = {
            activity
        }
    }

    constructor(fragment: Fragment) {
        permissionsRequest =
            fragment.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                ::onPermissionResult
            )
        startSettingRequest =
            fragment.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ::onSettingResult
            )
        getActivity = {
            fragment.requireActivity()
        }
        getFragmentManager = {
            fragment.childFragmentManager
        }
        getViewLifecycleOwner = {
            fragment.viewLifecycleOwner
        }
    }

    private fun onPermissionResult(map: Map<String, Boolean>) {
        permissionHandler.onActivityResult(map)
    }

    private fun onSettingResult(result: ActivityResult) {
        launch()
    }

    private fun afterPermissionGranted() {
        ZLog.i(ZTag.TAG, "afterPermissionGranted")
        onPermissionGranted?.invoke()
    }

    /**
     * 获取权限前
     */
    private fun beforePermissionGranted() {
        ZLog.i(ZTag.TAG, "beforePermissionGranted")
        beforePermissionGranted?.invoke()
    }

    /**
     * 部分权限被拒绝
     */
    private fun onSomePermanentlyDenied() {
        val builder = DialogProvider.Builder()
            .title("请求权限")
            .message("如果没有「${permissionHandler.getDeniedPermissionNames()}」相关权限，此应用可能无法正常工作。")
            .negative("取消") { _, _ ->
                getActivity().finish()
            }
        builder.positive { _, _ ->
            ZLog.i(ZTag.TAG, "打开设置")
            permissionHandler.openSettings(startSettingRequest)
            // 打开设置
        }
        builder.build().show(getFragmentManager())
    }

    private fun onPermissionDenied() {
        launch()
    }

    private fun hasPermissions(): Boolean {
        return permissionHandler.hasPermissions()
    }

    fun onPermissionGranted(callback: PermissionCallback): PermissionRequest {
        onPermissionGranted = callback
        return this
    }

    fun beforePermissionGranted(callback: PermissionCallback): PermissionRequest {
        beforePermissionGranted = callback
        return this
    }
//    fun onSomePermanentlyDenied(callback: PermissionCallback): PermissionRequest {
//        onSomePermanentlyDenied = callback
//        return this
//    }
//
//    fun onPermissionDenied(callback: PermissionCallback): PermissionRequest {
//        onPermissionDenied = callback
//        return this
//    }

    fun request(permissions: Array<String>) {
        permissionHandler.updatePermission(permissions)
        launch()
    }

    private fun launch() {
        if (hasPermissions()) {
            afterPermissionGranted()
        } else {
            beforePermissionGranted()
            permissionHandler.launch(permissionsRequest)
        }
    }
}