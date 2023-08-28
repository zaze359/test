package com.zaze.core.designsystem.compose.components

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.*


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequired(
    permission: String,
    permissionNotGrantedContent: @Composable (PermissionState) -> Unit,
    permissionNotAvailableContent: @Composable (PermissionState) -> Unit,
    permissionGrantedContent: @Composable (PermissionState) -> Unit
) {
    // 特殊处理 文件读写权限
    val requestPermission = dealExternalStoragePermission(permission)
    // flag 记录是否已经请求过权限，保证shouldShowRationale能获取到值。
    val flag = remember {
        mutableStateOf(0)
    }
    val permissionState = rememberPermissionState(
        permission = requestPermission
    ) {// 申请权限后回调
        Log.d("onPermissionResult", "onPermissionResult: $it;}")
        if (!it) {
            flag.value++
        }
    }
    when {
        permissionState.status.isGranted -> {
            // 获取到权限
            permissionGrantedContent(permissionState)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Manifest.permission.MANAGE_EXTERNAL_STORAGE == requestPermission -> {
            if (Environment.isExternalStorageManager()) {
                permissionGrantedContent(permissionState)
            } else {
                permissionNotAvailableContent(permissionState)
            }
        }

        flag.value > 0 && !permissionState.status.shouldShowRationale -> {
            // 权限被永久拒绝
            permissionNotAvailableContent(permissionState)
        }
        else -> {
            // 当前无权限
            permissionNotGrantedContent(permissionState)
            // 申请权限
            // permissionState.launchPermissionRequest()
        }
    }
}


private fun dealExternalStoragePermission(permission: String): String {
    return when (permission) {
        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            } else {
                permission
            }
        }

        Manifest.permission.READ_EXTERNAL_STORAGE -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            } else {
                permission
            }
        }

        else -> {
            permission
        }
    }
}