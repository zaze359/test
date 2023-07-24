package com.zaze.demo.feature.communication.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.zaze.demo.feature.communication.CommunicationRoute

const val communicationRoute = "/demo/communication"

fun NavController.navigateToCommunication(navOptions: NavOptions? = null) {
    this.navigate(route = communicationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.communicationScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable(communicationRoute) {
        CommunicationRoute(
            onBackPress = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState,
        )
    }
}