package com.zaze.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Deprecated("")
@Composable
fun Padding(
    start: Int = 0,
    top: Int = 0,
    end: Int = 0,
    bottom: Int = 0,
    content: @Composable BoxScope.() -> Unit = {}
) {
//    Divider(
//        modifier = Modifier.padding(start, top, end, bottom),
//        thickness = (top - end).coerceAtLeast(0.dp),
//        color = Color.Transparent
//    )
    Box(
        modifier = Modifier
            .height((top + end).dp)
            .padding(start = start.dp, bottom = bottom.dp),
        content = content
    )
}

//interface PaddingScope: BoxScope {
//}
//
//object PaddingScopeInstance : PaddingScope {
//
//    @Stable
//    override fun Modifier.align(alignment: Alignment) = BoxScopeInstance
//
//    @Stable
//    override fun Modifier.matchParentSize() = this
//}


@Preview
@Composable
fun PaddingPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.Red.copy(alpha = 0.08f))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color.White)
        )
        Padding(10, 25, 25, 10)
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color.White)
        )
    }
}