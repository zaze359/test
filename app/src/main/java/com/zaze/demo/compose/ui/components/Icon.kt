package com.zaze.demo.compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection


/**
 * Description :
 * @author : zaze
 * @version : 2023-01-15 00:41
 */

@Composable
fun NavIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.Menu,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )

//    val semantics = if (contentDescription != null) {
//        Modifier.semantics {
//            this.contentDescription = contentDescription
//            this.role = Role.Image
//        }
//    } else {
//        Modifier
//    }
//
//    Box(modifier = modifier.then(semantics)) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_jetchat_back),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primaryContainer
//        )
//        Icon(
//            painter = painterResource(id = R.drawable.ic_jetchat_front),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primary
//        )
//    }
}

@Composable
fun mirroringBackIcon() = mirroringIcon(
    ltrIcon = Icons.Outlined.ArrowBack, rtlIcon = Icons.Outlined.ArrowForward
)

@Composable
private fun mirroringIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if (LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon


@Preview(name = "Nav icon")
@Composable
fun NavIconPreview() {
    NavIcon()
}

@Preview(name = "mirroringIcon")
@Composable
fun MirroringIconPreview() {
    Icon(imageVector = mirroringBackIcon(), contentDescription = null)
}