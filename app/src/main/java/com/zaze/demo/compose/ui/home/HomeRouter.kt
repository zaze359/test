package com.zaze.demo.compose.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaze.demo.compose.ui.SampleNavigationActions
import com.zaze.demo.model.entity.TableEntity

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-12 01:21
 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRouter(
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    navigationActions: SampleNavigationActions,
    startActivity: (TableEntity) -> Unit,
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    println("uiState: $uiState")
    HomeScreen(
        uiState = uiState,
        onTest = homeViewModel::onTest,
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        onErrorDismiss = homeViewModel::errorShown,
        navigateToSample = {
            it.navigationAction(navigationActions)
        },
        startActivity = startActivity
    )
}
