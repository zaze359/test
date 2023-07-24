package com.zaze.demo.feature.storage.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.zaze.demo.feature.storage.StorageRoute
import com.zaze.demo.feature.storage.datastore.DataStoreRoute
import com.zaze.demo.feature.storage.sp.SharedPreferencesRoute


const val storageRoute = "/demo/storage"
internal const val sharedPreferencesRoute = "sharedPreferences_route"
internal const val dataStoreRoute = "dataStore_route"

fun NavController.navigateToStorage(navOptions: NavOptions? = null) {
    this.navigate(storageRoute, navOptions)
}

fun NavGraphBuilder.storageScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable(storageRoute) {
        StorageRoute(
            onBackPress = {
                navController.popBackStack()
            },
            onNavigateToDestination = {
                val navOptions = navOptions {
                    popUpTo(storageRoute) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                navController.navigateToDestination(it, navOptions)
            }
        )
    }
    composable(sharedPreferencesRoute) {
        SharedPreferencesRoute(
            onBackPress = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState
        )
    }
    composable(dataStoreRoute) {
        DataStoreRoute(
            onBackPress = {
                navController.popBackStack()
            },
            snackbarHostState = snackbarHostState
        )
    }
}


//internal fun NavController.navigateToSharedPreferences(navOptions: NavOptions? = null) {
//    navigateToDestination(storageRoute, navOptions)
//}

internal fun NavController.navigateToDestination(route: String, navOptions: NavOptions? = null) {
    this.navigate(route, navOptions)
}