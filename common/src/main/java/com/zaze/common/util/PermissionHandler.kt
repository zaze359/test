package com.zaze.common.util

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.zaze.utils.log.ZLog

/**
 * 权限处理器
 */
class PermissionHandler(
    private val activity: Activity,
    /**
     * 所需权限
     */
    permissions: Array<String>,
    private val afterPermissionGranted: (() -> Unit)? = null,
    private val onSomePermanentlyDenied: (() -> Unit)? = null,
    private val onPermissionDenied: (() -> Unit)? = null
) :
    ActivityResultCallback<Map<String, @JvmSuppressWildcards Boolean>> {

    companion object {
        private const val TAG = "permission"
    }
    /**
     * 已获取到的权限
     */
    private val grantedPermissions = ArrayList<String>()

    /**
     * 被拒绝权限
     */
    val deniedPermissions = ArrayList<String>()

    init {
        when {
            permissions.isEmpty() -> {

            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                grantedPermissions.addAll(permissions)
            }
            else ->{
                permissions.forEach { permission ->
                    val isGranted = PermissionHelper.checkSelfPermission(activity, permission)
                    ZLog.i(TAG, "检查 $permission：$isGranted")
                    if (isGranted) {
                        grantedPermissions.add(permission)
                    } else {
                        deniedPermissions.add(permission)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    deniedPermissions.remove(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private val needExternalStoragePermission by lazy {
        permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) || permissions.contains(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


    override fun onActivityResult(resultMap: Map<String, Boolean>) {
        // 是否获得了权限
        var permissionAllGranted = true
        // 是否有权限被永久拒绝，默认false
        var permanentlyDenied = false
        resultMap.forEach { result ->
            val isGranted = result.value
            val permission = result.key
            ZLog.i(TAG, "${permission}: $isGranted")
            if (isGranted) {
                grantedPermissions.add(permission)
                deniedPermissions.remove(permission)
            } else {
                deniedPermissions.add(permission)
            }
            if (permissionAllGranted) {
                permissionAllGranted = isGranted
            }
            // 没有被永久拒绝的权限 && 当前权限被拒绝 && 不需要解释
            if (!isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    result.key
                )
            ) {
                // 存在权限被永久拒绝
                ZLog.i(TAG, "${permission}: 被永久拒绝")
                if (!permanentlyDenied) {
                    permanentlyDenied = true
                }
            }
        }
        when {
            permissionAllGranted -> {
                afterPermissionGranted?.invoke()
            }
            permanentlyDenied -> {
                onSomePermanentlyDenied?.invoke()
            }
            else -> {
                onPermissionDenied?.invoke()
            }
        }
    }

    fun launch(permissionsRequest: ActivityResultLauncher<Array<String>>) {
        if (!hasPermissions()) {
            permissionsRequest.launch(deniedPermissions.toTypedArray())
        }
    }

    fun getDeniedPermissionNames(): String {
        return PermissionHelper.getPermissionNames(deniedPermissions.toTypedArray())
    }

    fun hasPermissions(): Boolean {
        return deniedPermissions.isEmpty()
    }

}