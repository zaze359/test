package com.zaze.common.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-22 - 14:27
 */
object PermissionHelper {
    private val permissionMap = HashMap<String, String>().apply {
        this[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "访问存储空间"
        this[Manifest.permission.READ_PHONE_STATE] = "读取手机状态"
        this[Manifest.permission.ACCESS_FINE_LOCATION] = "获取位置"
    }

    fun getPermissionNames(permissions: Array<String>): String {
        val rationaleBuilder = StringBuilder()
        getPermissionNameSet(permissions).forEachIndexed { index, s ->
            if (index > 0) {
                rationaleBuilder.append(",$s")
            } else {
                rationaleBuilder.append(s)
            }
        }
        return rationaleBuilder.toString()
    }

    fun getPermissionNameSet(permissions: Array<String>): Collection<String> {
        val permissionNameSet = HashSet<String>()
        permissions.forEach {
            val permissionName = permissionMap[it]
            if (!permissionName.isNullOrEmpty()) {
                permissionNameSet.add(permissionName)
            }
        }
        return permissionNameSet
    }

    fun hasPermissions(context: Context, perms: Array<String>): Boolean {
        if (perms.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        perms.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun somePermissionPermanentlyDenied(
        activity: Activity,
        deniedPerms: Map<String, Boolean>
    ): Boolean {
        deniedPerms.forEach {
            if (!it.value && !shouldShowRequestPermissionRationale(activity, it.key)) {
                return true
            }
        }
        return false
    }

    fun somePermissionPermanentlyDenied(activity: Activity, deniedPerms: List<String>): Boolean {
        deniedPerms.forEach {
            if (!shouldShowRequestPermissionRationale(activity, it)) {
                return true
            }
        }
        return false
    }

    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    fun openSettingIntent(activity: Activity): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
            Uri.fromParts("package", activity.packageName, null)
        )
    }
}