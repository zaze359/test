package com.zaze.demo.compose.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-11 22:49
 */
object SampleDestinations {
    const val HOME_ROUTE = "home"
    const val SCAFFOLD_ROUTE = "scaffold"
}

class SampleNavigationActions(private val navController: NavHostController) {

    val navigateToHome: () -> Unit = {
        navigate(SampleDestinations.HOME_ROUTE)
    }

    val navigateToScaffold: () -> Unit = {
        navigate(SampleDestinations.SCAFFOLD_ROUTE)
    }

    private fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = ::defaultOptions) {
        navController.navigate(
            route = route, builder = builder
        )
    }

    private fun defaultOptions(builder: NavOptionsBuilder) {
        builder.popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        builder.launchSingleTop = true
        builder.restoreState = true
    }
}