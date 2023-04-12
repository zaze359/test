package com.zaze.demo.compose.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaze.core.designsystem.theme.MyComposeTheme
import com.zaze.core.designsystem.theme.MyTypography
import com.zaze.demo.R
import com.zaze.demo.compose.data.Menu
import com.zaze.demo.compose.data.drawerMenus
import com.zaze.demo.compose.navigation.homeRoute

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-15 17:34
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppDrawer(
    modifier: Modifier = Modifier,
    currentRoute: String,
    menus: List<Menu> = drawerMenus,
    closeDrawer: () -> Unit,
    onClicked: (Menu) -> Unit,
) {
    ModalDrawerSheet(modifier) {
        Row(
            Modifier
                .padding(horizontal = 28.dp, vertical = 24.dp)
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .clip(CircleShape) // 保证 graphicsLayer 不会绘制到 边界之外
                .graphicsLayer {
                    clip = true
                    shape = CircleShape
                }
                .background(Color(0xFFEBE1E4))) {
                Image(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = "",
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Test",
                style = MyTypography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(Modifier.height(12.dp))
        val paddingSizeModifier = Modifier
            .size(24.dp)
        menus.forEach { menu ->
            NavigationDrawerItem(
                icon = {
                    Image(
                        painter = painterResource(id = menu.icon),
                        modifier = paddingSizeModifier.then(Modifier.clip(CircleShape)),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                },
                label = {
                    Text(
                        stringResource(id = menu.label),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                selected = false,
                onClick = {
                    onClicked(menu)
//                    closeDrawer()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Preview
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyAppDrawerPreview() {
    MyComposeTheme() {
        MyAppDrawer(
            currentRoute = homeRoute,
            closeDrawer = {},
            onClicked = {},
        )
    }
}