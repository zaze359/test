package com.zaze.common.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-22 - 14:27
 */
object PermissionHelper {
    private val permissionMap by lazy {
        mutableMapOf(
            Manifest.permission.READ_EXTERNAL_STORAGE to "访问存储空间",
            Manifest.permission.WRITE_EXTERNAL_STORAGE to "访问存储空间",
            Manifest.permission.READ_PHONE_STATE to "读取手机状态",
            Manifest.permission.ACCESS_FINE_LOCATION to "获取位置",
            Manifest.permission.CAMERA to "相机",
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this[Manifest.permission.READ_MEDIA_IMAGES] = "访问图片和照片"
                this[Manifest.permission.READ_MEDIA_VIDEO] = "访问视频"
                this[Manifest.permission.READ_MEDIA_AUDIO] = "访问音频文件"
            }
        }
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
        return checkSelfPermission(context, perms)
    }

    fun checkSelfPermission(context: Context, perms: Array<String>): Boolean {
        if (perms.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return perms.find {
            !checkSelfPermission(context, it)
        } == null
    }

    fun checkSelfPermission(context: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
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

    fun createSettingIntent(context: Context, permission: String): Intent {
        return when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE -> {
                ExternalStoragePermission.createSettingIntent(context)
            }

            else -> {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.fromParts("package", context.packageName, null)
                )
            }
        }
    }
}