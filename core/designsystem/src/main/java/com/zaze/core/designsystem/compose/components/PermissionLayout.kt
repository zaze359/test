package com.zaze.core.designsystem.compose.components

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionLayout(
    permission: String,
    onPermissionGranted: (String) -> Unit,
    onPermissionNotAvailable: suspend CoroutineScope.(String) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    // 特殊处理 文件读写权限
    val requestPermission = dealExternalStoragePermission(permission)
    val permissionFlag = remember {
        mutableStateOf(0)
    }
    val permissionState = rememberPermissionState(permission = requestPermission) {
        Log.d("PermissionLayout", "onPermissionResult: $it;")
        if (!it && Manifest.permission.MANAGE_EXTERNAL_STORAGE != requestPermission) {
            permissionFlag.value++
        }
    }
    content {
        when {
            permissionState.status is PermissionStatus.Granted -> {
                onPermissionGranted(permission)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Manifest.permission.MANAGE_EXTERNAL_STORAGE == requestPermission -> {
                if (Environment.isExternalStorageManager()) {
                    onPermissionGranted(permission)
                } else {
                    permissionFlag.value++
                }
            }

            else -> {
                permissionState.launchPermissionRequest()
            }
        }
    }

    LaunchedEffect(key1 = permissionFlag.value) {
        val status = permissionState.status
        if (permissionFlag.value > 0 && status is PermissionStatus.Denied && !status.shouldShowRationale) {
            Log.i("PermissionLayout", "onPermissionNotAvailable: $permission")
            onPermissionNotAvailable(permission)
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