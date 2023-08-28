package com.zaze.common.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.widget.dialog.DialogProvider
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

/**
 * 权限请求
 * 需要在 Activity STARTED 前；或者 Fragment created 前。
 */
class PermissionRequest {
    //    private var permissions: Array<String> = emptyArray()
    private val permissionsRequest: ActivityResultLauncher<Array<String>>
    private val startSettingRequest: ActivityResultLauncher<Intent>

    private var onPermissionGranted: PermissionCallback? = null
//    private var onSomePermanentlyDenied: PermissionCallback? = null
//    private var onPermissionDenied: PermissionCallback? = null


    private val getFragmentManager: () -> FragmentManager
    private val getViewLifecycleOwner: () -> LifecycleOwner
    private lateinit var getActivity: () -> Activity

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
        if (hasPermission()) {
            afterPermissionGranted()
        } else {
            setupPermission()
        }
    }


    private val permissionHandler by lazy {
        PermissionHandler(
            activity = getActivity(),
            permissions = emptyArray(),
            afterPermissionGranted = ::afterPermissionGranted,
            onSomePermanentlyDenied = ::onSomePermanentlyDenied,
            onPermissionDenied = ::onPermissionDenied
        )
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
    }

    /**
     * 部分权限被拒绝
     */
    private fun onSomePermanentlyDenied() {
        val builder = DialogProvider.Builder()
            .message("如果没有「${permissionHandler.getDeniedPermissionNames()}」相关权限，此应用可能无法正常工作。")
            .negative("取消") {
                getActivity().finish()
            }
        builder.positive {
            ZLog.i(ZTag.TAG, "openApplicationDetailsSetting")
            // 打开设置
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                Uri.fromParts("package", getActivity().packageName, null)
            )
            startSettingRequest.launch(intent)
        }
        builder.build().show(getFragmentManager())
    }

    private fun onPermissionDenied() {
        setupPermission()
    }

    private fun hasPermission(): Boolean {
        return permissionHandler.hasPermissions()
    }

    private fun setupPermission() {
        val viewLifecycleOwner = getViewLifecycleOwner()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (hasPermission()) {
                    afterPermissionGranted()
                } else {
                    beforePermissionGranted()
                    permissionHandler.launch(permissionsRequest)
                }
            }
        }
    }

    fun onPermissionGranted(callback: PermissionCallback): PermissionRequest {
        onPermissionGranted = callback
        return this
    }
//    fun onSomePermanentlyDenied(callback: PermissionCallback): ZPermission {
//        onSomePermanentlyDenied = callback
//        return this
//    }
//    fun onPermissionDenied(callback: PermissionCallback): ZPermission {
//        onPermissionDenied = callback
//        return this
//    }

    fun request(permissions: Array<String>) {
        permissionHandler.updatePermission(permissions)
        permissionHandler.launch(permissionsRequest)
    }

    fun hasPermissions(): Boolean {
        return permissionHandler.hasPermissions()
    }
}