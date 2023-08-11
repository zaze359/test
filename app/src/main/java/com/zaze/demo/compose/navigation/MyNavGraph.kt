package com.zaze.demo.compose.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zaze.demo.feature.communication.navigation.communicationScreen
import com.zaze.demo.feature.intent.navigation.intentScreen
import com.zaze.demo.feature.storage.navigation.storageScreen
import com.zaze.demo.update.appUpdateScreen

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-11 22:47
 */

@Composable
fun MyNavGraph(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit = {},
    startDestination: String = homeRoute,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            snackbarHostState = snackbarHostState,
            openDrawer = openDrawer,
            navController = navController,
            destinations = destinations,
            onNavigateToDestination = onNavigateToDestination
        )
        samplesScreen(
            isExpandedScreen = isExpandedScreen,
            navController = navController,
            snackbarHostState = snackbarHostState
        )
        communicationScreen(
            navController = navController,
            snackbarHostState = snackbarHostState
        )
        intentScreen(
            navController = navController,
            snackbarHostState = snackbarHostState
        )
        storageScreen(
            navController = navController,
            snackbarHostState = snackbarHostState
        )
        appUpdateScreen(navController = navController, snackbarHostState = snackbarHostState)
    }
}