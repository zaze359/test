package com.zaze.demo.compose.navigation

import androidx.compose.material.icons.rounded.Upgrade
import com.zaze.core.designsystem.compose.icon.Icon
import com.zaze.core.designsystem.compose.icon.MyIcons
import com.zaze.demo.R

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {
    HOME(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.ViewDay),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.ViewDay),
        iconTextId = R.string.home,
        titleTextId = R.string.home
    ),
    SCAFFOLD(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        iconTextId = R.string.scaffold,
        titleTextId = R.string.scaffold
    ),
    COMMUNICATION(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        iconTextId = R.string.communication,
        titleTextId = R.string.communication
    ),
    INTENT(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        iconTextId = R.string.intent,
        titleTextId = R.string.intent
    ),
    STORAGE(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Person),
        iconTextId = R.string.storage,
        titleTextId = R.string.storage
    ),
    APP_UPDATE(
        selectedIcon = Icon.ImageVectorIcon(MyIcons.Upgrade),
        unselectedIcon = Icon.ImageVectorIcon(MyIcons.Upgrade),
        iconTextId = R.string.app_update,
        titleTextId = R.string.app_update
    )
}