package com.zaze.demo.compose.navigation

import com.zaze.demo.R
import com.zaze.demo.compose.ui.components.Icon
import com.zaze.demo.compose.ui.components.MyIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {
    ONE(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.ViewDay),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.ViewDay),
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name
    ),
    TWO(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name
    ),
    THREE(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Grid3x3),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Grid3x3),
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name
    )
}