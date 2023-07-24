package com.zaze.demo.feature.intent.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.zaze.demo.feature.intent.IntentRoute

const val intentRoute = "/demo/intent"

fun NavController.navigateToIntent(navOptions: NavOptions? = null) {
    this.navigate(route = intentRoute, navOptions = navOptions)
}

fun NavGraphBuilder.intentScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable(intentRoute) {
        IntentRoute(
            onBackPress = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState,
        )
    }
}
