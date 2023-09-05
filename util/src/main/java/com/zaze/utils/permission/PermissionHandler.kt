package com.zaze.utils.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.zaze.utils.IntentFactory
import com.zaze.utils.log.ZLog


typealias PermissionCallback = () -> Unit

/**
 * 权限处理器
 */
class PermissionHandler(
    private val activity: Activity,
    /**
     * 所需权限
     */
    private var permissions: Array<String>,
    private val afterPermissionGranted: PermissionCallback? = null,
    private val onSomePermanentlyDenied: PermissionCallback? = null,
    private val onPermissionDenied: PermissionCallback? = null
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
    private val deniedPermissions = ArrayList<String>()

    init {
        updatePermission(permissions)
    }

    fun updatePermission(permissions: Array<String>) {
        //
        this.permissions = permissions
        //
        grantedPermissions.clear()
        deniedPermissions.clear()
        when {
            permissions.isEmpty() -> {

            }

            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                grantedPermissions.addAll(permissions)
            }

            else -> {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    deniedPermissions.remove(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private val needExternalStoragePermission
        get() = permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) || permissions.contains(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    override fun onActivityResult(result: Map<String, Boolean>) {
        // 是否获得了权限
        var permissionAllGranted = true
        // 是否有权限被永久拒绝，默认false
        var permanentlyDenied = false
        result.forEach { ret ->
            val isGranted = ret.value
            val permission = ret.key
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
                    ret.key
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
                if (needExternalStoragePermission && !ExternalStoragePermission.hasPermission(
                        activity
                    )
                ) {
                    onSomePermanentlyDenied?.invoke()
                } else {
                    afterPermissionGranted?.invoke()
                }
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
        if (hasPermissions()) {
            afterPermissionGranted?.invoke()
        } else {
            permissionsRequest.launch(deniedPermissions.toTypedArray())
        }
    }

    fun openSettings(settingsLauncher: ActivityResultLauncher<Intent>) {
        if (needExternalStoragePermission && !ExternalStoragePermission.hasPermission(activity)) {
            settingsLauncher.launch(ExternalStoragePermission.createSettingIntent(activity))
        } else {
            settingsLauncher.launch(IntentFactory.applicationDetailsSettings(activity.packageName))
        }
    }

    fun getDeniedPermissionNames(): String {
        val list = deniedPermissions.toMutableList()
        if (needExternalStoragePermission && !ExternalStoragePermission.hasPermission(activity)) {
            // 需要 外部存储，但是当前没有权限，重新将权限添加进入，以便获取到正确的提示内容。
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return PermissionHelper.getPermissionNames(list.toTypedArray())
    }

    fun hasPermissions(): Boolean {
        if (deniedPermissions.isEmpty()) {
            if (needExternalStoragePermission && !ExternalStoragePermission.hasPermission(activity)) {
                return false
            }
            return true
        }
        return false
    }
}