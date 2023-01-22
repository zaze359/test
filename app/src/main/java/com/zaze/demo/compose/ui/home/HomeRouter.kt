package com.zaze.demo.compose.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.zaze.demo.model.entity.TableEntity

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-12 01:21
 */

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRouter(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    navController: NavHostController,
    startActivity: (TableEntity) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
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
            it.navigationAction(navController)
        },
        startActivity = startActivity
    )
}
