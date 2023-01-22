package com.zaze.demo.compose.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zaze.demo.model.entity.TableEntity

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-11 22:47
 */

@Composable
fun SampleNavGraph(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit = {},
    startDestination: String = SampleDestinations.HOME_ROUTE,
    snackbarHostState: SnackbarHostState,
    startActivity: (TableEntity) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            snackbarHostState = snackbarHostState,
            openDrawer = openDrawer,
            navController = navController,
            startActivity = startActivity,
        )
        scaffoldScreen(
            isExpandedScreen = isExpandedScreen,
            navController = navController,
            snackbarHostState = snackbarHostState
        )
    }
}