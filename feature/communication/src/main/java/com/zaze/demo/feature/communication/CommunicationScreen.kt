package com.zaze.demo.feature.communication

import android.content.*
import android.os.Build
import android.os.IBinder
import android.os.Messenger
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zaze.core.designsystem.compose.icon.mirroringBackIcon
import com.zaze.core.designsystem.compose.components.snackbar.MySnackbarHost
import com.zaze.core.model.data.ChatMessage
import com.zaze.demo.feature.communication.aidl.RemoteService
import com.zaze.demo.feature.communication.broadcast.MessageReceiver
import com.zaze.demo.feature.communication.conversation.ConversationMessages
import com.zaze.demo.feature.communication.conversation.IpcInput
import com.zaze.demo.feature.communication.messenger.MessengerService
import com.zaze.demo.feature.communication.parcel.IpcMessage
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

enum class CommunicationMode {
    /**
     * 使用 AIDL 方式通讯
     */
    AIDL,

    /**
     * 使用 Messenger 方式通讯
     */
    MESSENGER,

    /**
     * 使用 Broadcast 方式通讯
     */
    BROADCAST,
    SOCKET,

    /**
     * 和 服务端 通讯
     */
    SERVER,
}


@Composable
internal fun CommunicationRoute(
    viewModel: CommunicationViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBackPress: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current // 当前这个Composable的生命周期
) {
    // 连接 messengerService
    val messengerServiceConnection = remember {
        mutableStateOf(object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.serviceMessenger = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                viewModel.serviceMessenger = Messenger(service)
            }
        })
    }

    // 连接 remoteService
    val remoteServiceServiceConnection = remember {
        mutableStateOf(object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.onRemoteServiceDisconnected()
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                viewModel.onRemoteServiceConnected(service)
            }
        })
    }

    val replayReceiver = remember {
        mutableStateOf(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == MessageReceiver.ACTION_REPLAY) {
                    val ipcMessage: IpcMessage? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                MessageReceiver.KEY_MESSAGE,
                                IpcMessage::class.java
                            )
                        } else {
                            intent.getParcelableExtra(MessageReceiver.KEY_MESSAGE)
                        }
                    viewModel.addChatMessage(
                        ChatMessage.Text(
                            author = "BROADCAST",
                            content = ipcMessage?.data ?: "",
                        )
                    )
                }
            }

            fun register(context: Context) {
                context.registerReceiver(this, IntentFilter(MessageReceiver.ACTION_REPLAY))
            }

            fun unRegister(context: Context) {
                context.unregisterReceiver(this)
            }
        })
    }
    val context = LocalContext.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            ZLog.i("IpcScreen", "event: $event")
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    ZLog.i("IpcScreen", "bindService")
                    context.bindService(
                        Intent(context, MessengerService::class.java),
                        messengerServiceConnection.value,
                        Context.BIND_AUTO_CREATE
                    )
                    val serviceIntent = Intent("com.zaze.export.remoteService")
                    serviceIntent.setPackage("com.zaze.demo")
//                    serviceIntent.component = ComponentName(
//                        "com.zaze.demo",
//                        "com.zaze.demo.feature.communication.aidl.RemoteService"
//                    )
                    context.bindService(
                        serviceIntent,
                        remoteServiceServiceConnection.value,
                        Context.BIND_AUTO_CREATE
                    )
                    //
                    replayReceiver.value.register(context)
                }

                else -> {
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { // 解绑
            ZLog.i("IpcScreen", "解绑")
            lifecycleOwner.lifecycle.removeObserver(observer)
            context.unbindService(messengerServiceConnection.value)
            context.unbindService(remoteServiceServiceConnection.value)
            replayReceiver.value.unRegister(context)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    CommunicationScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackPress = onBackPress,
        onSwitchMode = viewModel::onSwitchMode,
        onMessageSent = viewModel::sendMessage,
        onPictureSend = viewModel::sendPicture,
        onFileSend = viewModel::onFileSend,
        openCamera = {
            ZLog.i(ZTag.TAG, "openCamera")
            coroutineScope.launch {
                snackbarHostState.showSnackbar("openCamera")
            }
        },
        openPhoto = {
            ZLog.i(ZTag.TAG, "openPhoto")
            coroutineScope.launch {
                snackbarHostState.showSnackbar("openPhoto")
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
internal fun CommunicationScreen(
    uiState: CommunicationUiState,
    snackbarHostState: SnackbarHostState,
    onBackPress: () -> Unit,
    onMessageSent: (String) -> Unit,
    onPictureSend: (String) -> Unit,
    onFileSend: (List<String>) -> Unit,
    onSwitchMode: (CommunicationMode) -> Unit,
    openCamera: () -> Unit,
    openPhoto: () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        snackbarHost = { MySnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.communication),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = mirroringBackIcon(),
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        }) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        IpcConversation(
            uiState = uiState,
            modifier = screenModifier,
            onMessageSent = onMessageSent,
            onPictureSend = onPictureSend,
            onFileSend = onFileSend,
            onSwitchMode = onSwitchMode,
            openCamera = {
            },
            openPhoto = {
                openPhoto()
            },
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
private fun IpcConversation(
    uiState: CommunicationUiState,
    onMessageSent: (String) -> Unit,
    onPictureSend: (String) -> Unit,
    onFileSend: (List<String>) -> Unit,
    onSwitchMode: (CommunicationMode) -> Unit,
    snackbarHostState: SnackbarHostState,
    openCamera: () -> Unit,
    openPhoto: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConversationMessages(
            me = uiState.me,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            messages = uiState.messages,
            scrollState = scrollState
        )
        // 选择IPC通信方式
        IpcModeSelector(onSwitchMode = onSwitchMode, currentMode = uiState.communicationMode)
        // 输入、发送消息
        IpcInput(
            onMessageSent = onMessageSent,
            onPictureSend = onPictureSend,
            onFileSend = onFileSend,
            resetScroll = {
                scope.launch {
                    scrollState.scrollToItem(0)
                }
            },
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding() // 将输入面板，移到导航栏和 IME 之上
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IpcModeSelector(
    currentMode: CommunicationMode,
    onSwitchMode: (CommunicationMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        disabledContainerColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
    )
    LazyHorizontalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp),
        rows = StaggeredGridCells.Fixed(2),
        // Grid的内边距
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        // item间的垂直间距
        verticalArrangement = Arrangement.spacedBy(4.dp),
        // item间的水平间距
        horizontalItemSpacing = 8.dp
    ) {
        items(CommunicationMode.values()) {
            Button(
                enabled = currentMode != it,
                onClick = {
                    onSwitchMode(it)
                },
                colors = buttonColors,
                border = if (currentMode != it) BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ) else null,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = it.name,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
//    val buttonModifier = Modifier
//        .fillMaxWidth()
//        .padding(start = 12.dp, end = 12.dp)
//    ElevatedButton(
//        modifier = buttonModifier,
//        onClick = onMessageSent,
//    ) {
//        Text(text = "Test Messenger")
//    }
//    ElevatedButton(
//        modifier = buttonModifier,
//        onClick = onMessageSent,
//    ) {
//        Text(text = "Test AIDL")
//    }
//    onSwitchMode(IpcMode.AIDL)
}
