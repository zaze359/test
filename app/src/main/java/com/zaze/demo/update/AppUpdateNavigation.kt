package com.zaze.demo.update

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val appUpdateRoute = "/app/update"

fun NavController.navigateToAppUpdate(navOptions: NavOptions? = null) {
    this.navigate(route = appUpdateRoute, navOptions = navOptions)
}

fun NavGraphBuilder.appUpdateScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable(appUpdateRoute) {
        AppUpdateRoute(
            snackbarHostState = snackbarHostState,
            viewModel = hiltViewModel(),
            onBackPress = {
                navController.popBackStack()
            })
    }
}