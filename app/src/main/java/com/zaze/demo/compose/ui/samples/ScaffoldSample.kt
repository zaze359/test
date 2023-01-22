package com.zaze.demo.compose.ui.samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zaze.demo.compose.navigation.TopLevelDestination
import com.zaze.demo.compose.ui.components.Icon
import com.zaze.demo.compose.ui.components.MySnackbarHost
import com.zaze.demo.compose.ui.components.mirroringBackIcon

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-11 19:02
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSample(
    isExpandedScreen: Boolean, onBackPress: () -> Unit, snackbarHostState: SnackbarHostState
) {
    Scaffold(snackbarHost = { MySnackbarHost(hostState = snackbarHostState) }, topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "TopAppBar", style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                if (!isExpandedScreen) {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = mirroringBackIcon(),
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Open search */ }) {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "cd_search"
                    )
                }
            },
        )
    }, bottomBar = {
        MyBottomBar(
            currentDestination = TopLevelDestination.ONE.name,
            destinations = listOf(
                TopLevelDestination.ONE,
                TopLevelDestination.TWO,
                TopLevelDestination.THREE
            ),
            onNavigateToDestination = {}
        )
    }) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
//                InterestScreenContent(
//                    currentSection, isExpandedScreen,
//                    onTabChange, tabContent, screenModifier
//                )
        Box(
            modifier = screenModifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.background(Color.Gray),
                text = "Content",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun MyBottomBar(
    currentDestination: String,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
) {
    NavigationBar(
        tonalElevation = 0.dp,
    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Blue),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                modifier = Modifier.background(Color.Black),
//                text = "BottomAppBar",
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.titleLarge
//            )
//        }
        destinations.forEach { destination ->
            val selected = destination.name == currentDestination
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }

    }
}


private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Preview
@Composable
fun ScaffoldPreview() {
    ScaffoldSample(isExpandedScreen = false, onBackPress = {}, remember { SnackbarHostState() })
}