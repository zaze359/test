package com.zaze.demo.compose.data

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaze.demo.R

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-15 19:11
 */
sealed class Menu(
    val id: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
)

class Router(
    id: String,
    @StringRes label: Int,
    @DrawableRes icon: Int,
    val destination: String,
) : Menu(id = id, label = label, icon = icon)

class Action(
    id: String,
    @StringRes label: Int,
    @DrawableRes icon: Int,
    val intent: Intent
) : Menu(id = id, label = label, icon = icon)


val drawerMenus = listOf<Menu>(
    Action(
        id = "github_link",
        label = R.string.github_link,
        icon = R.drawable.ic_github_circle_white_24dp,
        intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://github.com/zaze359/test.git")
        }
    )
)