package com.zaze.demo.feature.communication.conversation

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.zaze.core.designsystem.compose.components.PermissionLayout
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean,
    permission: String,
    onPermissionNotAvailable: suspend CoroutineScope.(String) -> Unit
) {
    PermissionLayout(permission = permission, onPermissionGranted = {
        onClick()
    }, onPermissionNotAvailable = onPermissionNotAvailable) {
        InputSelectorButton(
            onClick = it,
            icon = icon,
            selected = selected,
            description = description
        )
    }

//    val context = LocalContext.current
//    val permissionFlag = remember {
//        mutableStateOf(0)
//    }
//    val permissionState = rememberPermissionState(permission = permission) {
//        Log.d("PermissionSelectorButton", "onPermissionResult: $it; ${ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)}"
//        )
//        if (!it) {
//            permissionFlag.value++
//        }
//    }
//
//    InputSelectorButton(
//        onClick = {
//            when (permissionState.status) {
//                PermissionStatus.Granted -> {
//                    onClick()
//                }
//                is PermissionStatus.Denied -> {
//                    permissionState.launchPermissionRequest()
//                }
//            }
//        },
//        icon = icon,
//        selected = selected,
//        description = description
//    )
//
//    LaunchedEffect(key1 = permissionFlag.value) {
//        val status = permissionState.status
//        if (permissionFlag.value > 0 && status is PermissionStatus.Denied && !status.shouldShowRationale) {
//            Log.i("Permission", "onPermissionNotAvailable: $permission")
//            onPermissionNotAvailable(permission)
//        }
//    }
}

@Composable
fun InputSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean
) {
    val backgroundModifier = if (selected) {
        Modifier.background(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(14.dp)
        )
    } else {
        Modifier
    }
    IconButton(
        onClick = onClick,
        modifier = Modifier.then(backgroundModifier)
    ) {
        val tint = if (selected) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.secondary
        }
        Icon(
            icon,
            tint = tint,
            modifier = Modifier
                .padding(8.dp)
                .size(56.dp),
            contentDescription = description
        )
    }
}
