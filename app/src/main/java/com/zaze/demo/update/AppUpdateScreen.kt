package com.zaze.demo.update

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaze.common.permission.ExternalStoragePermission
import com.zaze.common.permission.PermissionHelper
import com.zaze.core.designsystem.compose.components.BackIconButton
import com.zaze.core.designsystem.compose.components.PermissionLayout

@Composable
fun AppUpdateRoute(
    snackbarHostState: SnackbarHostState,
    viewModel: AppUpdateViewModel,
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AppUpdateScreen(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onBackPress = onBackPress,
        unInstallApp = viewModel::unInstallApp,
        installOldApp = viewModel::installOldApp,
        applyPatch = viewModel::applyPatch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppUpdateScreen(
    snackbarHostState: SnackbarHostState,
    uiState: AppUpdateUiState,
    onBackPress: () -> Unit,
    unInstallApp: () -> Unit,
    installOldApp: () -> Unit,
    applyPatch: () -> Unit,
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "应用更新")
        }, navigationIcon = {
            BackIconButton(onBackPress = onBackPress)
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(modifier = Modifier.fillMaxWidth(), onClick = unInstallApp) {
                Text(text = "卸载应用")
            }
            ElevatedButton(modifier = Modifier.fillMaxWidth(), onClick = installOldApp) {
                Text(text = "安装旧版本")
            }
            ElevatedButton(modifier = Modifier.fillMaxWidth(), onClick = applyPatch) {
                Text(text = "应用增量包, 并更新")
            }
        }
        val context = LocalContext.current
        if (uiState is AppUpdateUiState.NoPermission) {
            val permission = remember(uiState) { uiState.permission }
            println("noPermission: $permission")
            PermissionLayout(
                permission = ExternalStoragePermission.getExternalStoragePermission(),
                onPermissionGranted = {

                },
                onPermissionNotAvailable = {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "需要访问存储空间权限",
                        actionLabel = "设置",
                        withDismissAction = true
                    )
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            PermissionHelper.createSettingIntent(context, permission)
                        }

                        SnackbarResult.Dismissed -> {
                        }
                    }
                }) {
            }
        }
    }
}