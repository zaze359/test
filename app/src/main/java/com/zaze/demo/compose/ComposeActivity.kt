package com.zaze.demo.compose

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zaze.common.base.AbsActivity
import com.zaze.common.util.ActivityUtil
import com.zaze.core.designsystem.theme.MyComposeTheme
import com.zaze.demo.compose.ui.MyAppDrawer
import com.zaze.demo.compose.data.Action
import com.zaze.demo.compose.data.Router
import com.zaze.demo.compose.navigation.MyNavGraph
import com.zaze.demo.compose.navigation.TopLevelDestination
import com.zaze.demo.compose.navigation.homeRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Description :
 * @author : zaze
 * @version : 2022-12-03 20:33
 */
@AndroidEntryPoint
class ComposeActivity : AbsActivity() {

    override fun getPermissionsToRequest(): Array<String> {
        return mutableListOf(
//            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_VIDEO)
                add(Manifest.permission.READ_MEDIA_IMAGES)
                add(Manifest.permission.READ_MEDIA_AUDIO)
            }
        }.toTypedArray()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            MyApp(
                windowSizeClass = windowSizeClass,
                onAction = {
                    ActivityUtil.startActivity(this, it)
                })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyApp(
    windowSizeClass: WindowSizeClass,
    onAction: (Intent) -> Unit,
    appState: MyAppState = rememberMyAppState(
        windowSizeClass = windowSizeClass
    )
) {
    MyComposeTheme {
        // true：横向中的大多数平板电脑和横向中的大型展开内部显示器（宽屏）
        val isExpandedScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
        val coroutineScope = rememberCoroutineScope()
        //
        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)
        //
        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: homeRoute
        ModalNavigationDrawer(drawerState = sizeAwareDrawerState,
            // 是否可用手势打开，
            gesturesEnabled = !isExpandedScreen && currentRoute == homeRoute,
            drawerContent = {
                MyAppDrawer(currentRoute = currentRoute,
                    closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
                    onClicked = {
                        when (it) {
                            is Router -> {
                                
                            }
                            is Action -> {
                                onAction(it.intent)
                            }
                        }
                    })
            }
        ) {
            MyNavGraph(
                snackbarHostState = appState.snackbarHostState,
                navController = appState.navController,
                isExpandedScreen = isExpandedScreen,
                openDrawer = {
                    coroutineScope.launch { sizeAwareDrawerState.open() }
                },
                destinations = appState.topLevelDestinations.toMutableList().apply {
                    remove(TopLevelDestination.HOME)
                },
                onNavigateToDestination = appState::navigateToTopLevelDestination,
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}