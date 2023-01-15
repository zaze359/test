package com.zaze.demo.compose.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zaze.common.base.AbsActivity
import com.zaze.common.util.ActivityUtil
import com.zaze.demo.compose.data.Action
import com.zaze.demo.compose.data.Router
import com.zaze.demo.compose.theme.MyComposeTheme
import com.zaze.demo.compose.ui.components.MyAppDrawer
import com.zaze.demo.model.entity.TableEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Description :
 * @author : zaze
 * @version : 2022-12-03 20:33
 */
@AndroidEntryPoint
class ComposeActivity : AbsActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            MyApp(widthSizeClass = widthSizeClass, startActivity = {
                ActivityUtil.startActivity(this, Intent(this, it.targetClass))
            }, onAction = {
                ActivityUtil.startActivity(this, it)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyApp(
    widthSizeClass: WindowWidthSizeClass,
    startActivity: (TableEntity) -> Unit,
    onAction: (Intent) -> Unit,
) {
    MyComposeTheme {
        val appState = rememberMyAppState()
        //
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            SampleNavigationActions(navController)
        }
        // true：横向中的大多数平板电脑和横向中的大型展开内部显示器（宽屏）
        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
        val coroutineScope = rememberCoroutineScope()
        //
        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)
        //
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: SampleDestinations.HOME_ROUTE
        ModalNavigationDrawer(drawerState = sizeAwareDrawerState,
            // 是否可用手势打开，
            gesturesEnabled = !isExpandedScreen && currentRoute == SampleDestinations.HOME_ROUTE,
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
            }) {
            SampleNavGraph(
                snackbarHostState = appState.snackbarHostState,
                navController = navController,
                isExpandedScreen = isExpandedScreen,
                openDrawer = {
                    println("openDrawer")
                    coroutineScope.launch { sizeAwareDrawerState.open() }
                },
                navigationActions = navigationActions,
                startActivity = startActivity
            )
        }
    }
}

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