package com.zaze.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.rounded.*
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

object MyIcons {
    val AccountCircle = Icons.Outlined.AccountCircle
    val Add = Icons.Rounded.Add
    val ArrowBack = Icons.Rounded.ArrowBack
    val ArrowDropDown = Icons.Default.ArrowDropDown
    val ArrowDropUp = Icons.Default.ArrowDropUp
    val Check = Icons.Rounded.Check
    val Close = Icons.Rounded.Close
    val ExpandLess = Icons.Rounded.ExpandLess
    val Fullscreen = Icons.Rounded.Fullscreen
    val Grid3x3 = Icons.Rounded.Grid3x3
    val MoreVert = Icons.Default.MoreVert
    val Person = Icons.Rounded.Person
    val PlayArrow = Icons.Rounded.PlayArrow
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
    val ShortText = Icons.Rounded.ShortText
    val Tag = Icons.Rounded.Tag
    val Upgrade = Icons.Rounded.Upgrade
    val ViewDay = Icons.Rounded.ViewDay
    val VolumeOff = Icons.Rounded.VolumeOff
    val VolumeUp = Icons.Rounded.VolumeUp
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}



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


