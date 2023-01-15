package com.zaze.demo.compose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaze.demo.R

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-15 00:28
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    onNavIconPressed: () -> Unit = { },
    actions: @Composable RowScope.() -> Unit = {},
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.enterAlwaysScrollBehavior(
        topAppBarState
    )
) {
    CenterAlignedTopAppBar(modifier = modifier,
        title = title,
        actions = actions,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            NavIcon(
                modifier = modifier
                    .size(64.dp)
                    .clickable(onClick = onNavIconPressed)
                    .padding(16.dp)
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyTopAppBarPreview() {
    MyTopAppBar(title = {
        Text(
            text = stringResource(id = R.string.compose_samples),
            style = MaterialTheme.typography.titleLarge
        )
    },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = "cd_search"
                )
            }
        })
}