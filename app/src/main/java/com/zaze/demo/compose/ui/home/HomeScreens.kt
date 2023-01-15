package com.zaze.demo.compose.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zaze.demo.R
import com.zaze.demo.compose.data.Sample
import com.zaze.demo.compose.theme.MyTypography
import com.zaze.demo.compose.ui.components.MySnackbarHost
import com.zaze.demo.compose.ui.components.MyTopAppBar
import com.zaze.demo.compose.ui.components.snackbar.Message
import com.zaze.demo.compose.ui.components.snackbar.toTextString
import com.zaze.demo.model.entity.TableEntity
import java.util.*

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-13 01:47
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onTest: () -> Unit,
    openDrawer: () -> Unit,
    navigateToSample: (Sample) -> Unit,
    startActivity: (TableEntity) -> Unit,
    onErrorDismiss: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = {
            MySnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            MyTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.compose_samples),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open search */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search, contentDescription = "cd_search"
                        )
                    }
                },
                onNavIconPressed = openDrawer,
            )
        },
    ) { innerPadding ->
        SampleList(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onTest = onTest,
            navigateToSample = navigateToSample,
            startActivity = startActivity
        )
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
        val errorMessageText = errorMessage.toTextString()

        val onErrorDismissState by rememberUpdatedState(newValue = onErrorDismiss)
        println("errorMessageText: $errorMessageText")
        println("snackbarHostState: ${snackbarHostState}")
        LaunchedEffect(errorMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText, actionLabel = "Retry", withDismissAction = true
            )
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    // 点击 snackbar actionLabel 触发事件时
                    println("SnackbarResult.ActionPerformed")
                }
                SnackbarResult.Dismissed -> {
                    println("SnackbarResult.Dismissed")
                }
            }
            println("snackbarResult: ${snackbarResult.name}")
            onErrorDismissState(errorMessage.id)
        }
    }
}

@Composable
private fun SampleList(
    modifier: Modifier,
    uiState: HomeUiState,
    onTest: () -> Unit,
    navigateToSample: (Sample) -> Unit,
    startActivity: (TableEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                onClick = onTest,
            ) {
                Text(text = stringResource(id = R.string.test))
            }
        }
        when (uiState) {
            is HomeUiState.NoPermission -> {
            }
            is HomeUiState.HasSamples -> {
                items(uiState.samples) { sample ->
                    SampleItem(sample.title) {
                        navigateToSample(sample)
                    }
                }
                items(uiState.activities) { activity ->
                    SampleItem(activity.name) {
                        startActivity(activity)
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(60.dp, Dp.Unspecified)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = title, style = MyTypography.titleMedium
        )
    }
    SampleListDivider()
}

@Composable
private fun SampleListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}


//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "Dark"
//)
//@Preview(showBackground = true, widthDp = 320)
@Preview
@Composable
fun HomeScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    var uiState by remember {
        mutableStateOf(
            HomeUiState.HasSamples(
                samples = emptyList(),
                activities = emptyList(),
                isLoading = false,
                errorMessages = emptyList()
            )
        )
    }
    HomeScreen(
        uiState,
        onTest = {
            val errorMessages = uiState.errorMessages + Message.WithString(messageText = "测试")
            uiState = uiState.copy(errorMessages = errorMessages)
            println("uiState: ${uiState.errorMessages}")
        },
        snackbarHostState = snackbarHostState,
        openDrawer = { },
        onErrorDismiss = {},
        navigateToSample = { sample ->
            println("navigateToSample: $sample")
        },
        startActivity = {
            println("startActivity: ${it.name}")
        },
    )
}