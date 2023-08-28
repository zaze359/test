package com.zaze.demo.feature.storage.datastore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaze.core.designsystem.compose.components.BackIconButton
import com.zaze.core.designsystem.compose.components.snackbar.MySnackbarHost


@Composable
internal fun DataStoreRoute(
    onBackPress: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: DataStoreViewModel = hiltViewModel()
) {
    val message by viewModel.userPreferencesFlow.collectAsStateWithLifecycle("empty")
    DataStoreScreen(
        message = message,
        snackbarHostState = remember { SnackbarHostState() },
        onBackPress = onBackPress,
        onUpdate = viewModel::update,
        onRemove = viewModel::remove,
        onClear = viewModel::clear,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataStoreScreen(
    message: String,
    snackbarHostState: SnackbarHostState,
    onBackPress: () -> Unit,
    onUpdate: () -> Unit,
    onRemove: () -> Unit,
    onClear: () -> Unit,
) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Indefinite
        )
    }
    Scaffold(
        snackbarHost = {
            MySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "DataStore")
            }, navigationIcon = {
                BackIconButton(onBackPress = onBackPress)
            })
        }) {
        Column(modifier = Modifier.padding(it)) {
            TestButton("update", onUpdate)
            TestButton("remove", onRemove)
            TestButton("clear", onClear)
        }
    }
}

@Composable
private fun TestButton(text: String, onClick: () -> Unit) {
    Button(modifier = Modifier.padding(12.dp, 8.dp).fillMaxWidth(), onClick = onClick) {
        Text(text = text)
    }
}