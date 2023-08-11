package com.zaze.demo.compose.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zaze.common.permission.ExternalStoragePermission
import com.zaze.common.util.ActivityUtil
import com.zaze.core.designsystem.components.MyTopAppBar
import com.zaze.core.designsystem.components.PermissionLayout
import com.zaze.core.designsystem.components.PermissionRequired
import com.zaze.core.designsystem.components.snackbar.MySnackbarHost
import com.zaze.core.designsystem.components.snackbar.SnackbarMessage
import com.zaze.core.designsystem.components.snackbar.toTextString
import com.zaze.core.designsystem.theme.MyTypography
import com.zaze.demo.R
import com.zaze.demo.compose.navigation.TopLevelDestination
import com.zaze.demo.data.entity.TableEntity
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.util.*

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-13 01:47
 */
@Composable
internal fun HomeRoute(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel(),
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
//    ZLog.i(ZTag.TAG_DEBUG, "uiState: $uiState")
    val context = LocalContext.current
    HomeScreen(
        uiState = uiState,
        onTest = homeViewModel::onTest,
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        onErrorDismiss = homeViewModel::errorShown,
        destinations = destinations,
        onNavigateToDestination = onNavigateToDestination,
        startActivity = {
            if (it.targetClass != null) {
                ActivityUtil.startActivity(context, Intent(context, it.targetClass))
            } else {
                ARouter.getInstance()
                    .build(it.route)
                    .navigation()
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onTest: () -> Unit,
    openDrawer: () -> Unit,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    startActivity: (TableEntity) -> Unit,
    onErrorDismiss: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = {
            MySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            MyTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.compose_samples),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open search */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search, contentDescription = "cd_search"
                        )
                    }
                },
                onNavIconPressed = openDrawer,
            )
        },
    ) { innerPadding ->
        SampleScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onTest = onTest,
            destinations = destinations,
            onNavigateToDestination = onNavigateToDestination,
            startActivity = startActivity
        )
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.toTextString()

        val onErrorDismissState by rememberUpdatedState(newValue = onErrorDismiss)
        println("errorMessageText: $errorMessageText")
        println("snackbarHostState: $snackbarHostState")
        LaunchedEffect(errorMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText, actionLabel = "Retry", withDismissAction = true
            )
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    // 点击 snackbar actionLabel 触发事件时
                    println("SnackbarResult.ActionPerformed")
                }

                SnackbarResult.Dismissed -> {
                    println("SnackbarResult.Dismissed")
                }
            }
            println("snackbarResult: ${snackbarResult.name}")
            onErrorDismissState(errorMessage.id)
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SampleScreen(
    modifier: Modifier,
    uiState: HomeUiState,
    onTest: () -> Unit,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    startActivity: (TableEntity) -> Unit,
) {
    val context = LocalContext.current
    val startSettingRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            ZLog.i("SampleScreen", "hasPermission: ${it.data}")
        }
    Column(modifier = modifier) {
        PermissionLayout(
            permission = ExternalStoragePermission.getExternalStoragePermission(),
            onPermissionGranted = {
                ZLog.i("SampleScreen", "permissionGrantedContent")
                onTest()
            },
            onPermissionNotAvailable = {
                ZLog.i("SampleScreen", "permissionNotAvailableContent")
                startSettingRequest.launch(ExternalStoragePermission.createSettingIntent(context))
            }) {
            testButton(it)
        }

//        SampleList(
//            uiState = uiState,
//            destinations = destinations,
//            onNavigateToDestination = onNavigateToDestination,
//            startActivity = startActivity
//        )
        SampleStaggeredGrid(
            uiState = uiState,
            destinations = destinations,
            onNavigateToDestination = onNavigateToDestination,
            startActivity = startActivity
        )
    }
}

@Composable
private fun testButton(onClick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        onClick = onClick,
    ) {
        Text(text = stringResource(id = R.string.test))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SampleStaggeredGrid(
    uiState: HomeUiState,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    startActivity: (TableEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            val itemModifier = Modifier
                .fillMaxWidth()
                .heightIn(60.dp, Dp.Unspecified)
                .padding(vertical = 10.dp, horizontal = 20.dp)
            when (uiState) {
                is HomeUiState.NoPermission -> {
                }

                is HomeUiState.HasSamples -> {
                    items(destinations) { destination ->
                        SampleItem(itemModifier, stringResource(id = destination.titleTextId)) {
                            onNavigateToDestination(destination)
                        }
                    }
                    items(uiState.activities) { activity ->
                        SampleItem(itemModifier, activity.name) {
                            startActivity(activity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleList(
    uiState: HomeUiState,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    startActivity: (TableEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val itemModifier = Modifier
                .fillMaxWidth()
                .heightIn(60.dp, Dp.Unspecified)
                .padding(vertical = 10.dp, horizontal = 20.dp)
            when (uiState) {
                is HomeUiState.NoPermission -> {
                }

                is HomeUiState.HasSamples -> {
                    items(destinations) { destination ->
                        SampleItem(itemModifier, stringResource(id = destination.titleTextId)) {
                            onNavigateToDestination(destination)
                        }
                    }
                    items(uiState.activities) { activity ->
                        SampleItem(itemModifier, activity.name) {
                            startActivity(activity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleItem(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = buttonColors,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    ) {
        Text(
            text = title,
            style = MyTypography.titleMedium,
        )
    }
//    SampleListDivider()
}

@Composable
private fun SampleListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}


//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "Dark"
//)
//@Preview(showBackground = true, widthDp = 320)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
fun HomeScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    var uiState by remember {
        mutableStateOf(
            HomeUiState.HasSamples(
                destinations = emptyList(),
                activities = emptyList(),
                isLoading = false,
                errorMessages = emptyList()
            )
        )
    }
    HomeScreen(
        uiState,
        onTest = {
            val errorMessages =
                uiState.errorMessages + SnackbarMessage.WithString(messageText = "测试")
            uiState = uiState.copy(errorMessages = errorMessages)
            ZLog.i(ZTag.TAG_DEBUG, "uiState: ${uiState.errorMessages}")
        },
        snackbarHostState = snackbarHostState,
        openDrawer = { },
        onErrorDismiss = {},
        destinations = TopLevelDestination.values().asList(),
        onNavigateToDestination = { destination ->
            ZLog.i(ZTag.TAG_DEBUG, "navigateToDestination: $destination")
        },
        startActivity = {
            ZLog.i(ZTag.TAG_DEBUG, "startActivity: ${it.name}")
        },
    )
}