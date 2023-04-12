package com.zaze.demo.compose.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.zaze.demo.compose.home.HomeRoute
import com.zaze.demo.compose.samples.ScaffoldSample

const val homeRoute = "home_route"
fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(route = homeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    navController: NavHostController,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    composable(homeRoute) {
        HomeRoute(
            snackbarHostState = snackbarHostState,
            openDrawer = openDrawer,
            destinations = destinations,
            onNavigateToDestination = onNavigateToDestination
        )
    }
}


const val scaffoldRoute = "scaffold_route"

fun NavGraphBuilder.samplesScreen(
    isExpandedScreen: Boolean,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable(scaffoldRoute) {
        ScaffoldSample(
            isExpandedScreen = isExpandedScreen,
            onBackPress = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState
        )
    }
}

fun NavController.navigateToScaffold(navOptions: NavOptions? = null) {
    this.navigate(route = scaffoldRoute, navOptions = navOptions)
}
