package com.zaze.demo.feature.storage.sp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaze.core.designsystem.components.BackIconButton
import com.zaze.core.designsystem.components.snackbar.MySnackbarHost


@Composable
internal fun SharedPreferencesRoute(
    onBackPress: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: SharedPreferencesViewModel = hiltViewModel()
) {
    SharedPreferencesScreen(
        snackbarHostState = snackbarHostState,
        onBackPress = onBackPress,
        onApply = viewModel::apply,
        onCommit = viewModel::commit,
        onRead = viewModel::read,
        onRemove = viewModel::remove,
        onClear = viewModel::clear,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SharedPreferencesScreen(
    snackbarHostState: SnackbarHostState,
    onBackPress: () -> Unit,
    onApply: () -> Unit,
    onCommit: () -> Unit,
    onRead: () -> Unit,
    onRemove: () -> Unit,
    onClear: () -> Unit,
) {
    Scaffold(
        snackbarHost = {
            MySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "SharedPreferences")
            }, navigationIcon = {
                BackIconButton(onBackPress = onBackPress)
            })
        }) {
        Column(modifier = Modifier.padding(it)) {
            TestButton("get", onRead)
            TestButton("apply", onApply)
            TestButton("commit", onCommit)
            TestButton("remove", onRemove)
            TestButton("clear", onClear)
        }
    }
}

@Composable
private fun TestButton(text: String, onClick: () -> Unit) {
    Button(modifier = Modifier.padding(12.dp, 8.dp), onClick = onClick) {
        Text(text = text)
    }
}