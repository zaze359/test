package com.zaze.demo.feature.communication

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.*


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequired(
    permissionNotGrantedContent: @Composable () -> Unit,
    permissionNotAvailableContent: @Composable () -> Unit,
    permissionGrantedContent: @Composable () -> Unit
) {
    // flag 记录是否已经请求过权限，保证shouldShowRationale能获取到值。
    val flag = remember {
        mutableStateOf(0)
    }
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    ) {// 申请权限后回调
        if (!it) {
            flag.value++
        }
    }
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            // 获取到权限
            permissionGrantedContent()
        }
        is PermissionStatus.Denied -> {
            if (flag.value > 0 && permissionState.status is PermissionStatus.Denied && !permissionState.status.shouldShowRationale) {
                // 权限被永久拒绝
                permissionNotAvailableContent()
            } else {
                // 当前无权限
                permissionNotGrantedContent()
                // 申请权限
                // permissionState.launchPermissionRequest()
            }
        }
    }
}
