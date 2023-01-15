package com.zaze.demo.compose.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zaze.demo.compose.ui.home.HomeRouter
import com.zaze.demo.compose.ui.home.HomeViewModel
import com.zaze.demo.compose.ui.samples.ScaffoldSample
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
    navigationActions: SampleNavigationActions,
    startActivity: (TableEntity) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(SampleDestinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeRouter(
                homeViewModel = homeViewModel,
                snackbarHostState = snackbarHostState,
                openDrawer = openDrawer,
                navigationActions = navigationActions,
                startActivity = startActivity,
            )
        }
        composable(SampleDestinations.SCAFFOLD_ROUTE) {
            ScaffoldSample(
                isExpandedScreen = isExpandedScreen,
                onBackPress = {
                    navController.popBackStack()
                },
                snackbarHostState = snackbarHostState
            )
        }
    }
}