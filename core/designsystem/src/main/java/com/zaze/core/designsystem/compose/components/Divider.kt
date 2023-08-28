package com.zaze.core.designsystem.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
) {
    Divider(
        modifier = modifier,
        thickness = thickness,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}


@Preview
@Composable
fun DefaultDividerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(color = Color.Red.copy(alpha = 0.08f))
        )
        DefaultDivider(modifier = Modifier.padding(horizontal = 14.dp), thickness = 20.dp)
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(color = Color.Red.copy(alpha = 0.08f))
        )
    }
}