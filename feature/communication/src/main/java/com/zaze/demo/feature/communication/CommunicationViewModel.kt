package com.zaze.demo.feature.communication

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.database.Cursor
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.Parcel
import android.provider.DocumentsContract
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.core.model.data.ChatMessage
import com.zaze.core.model.data.getMessageContent
import com.zaze.demo.component.socket.WebSocketManager
import com.zaze.demo.feature.communication.broadcast.MessageReceiver
import com.zaze.demo.feature.communication.messenger.MessengerService
import com.zaze.demo.feature.communication.parcel.IpcMessage
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(application: Application) :
    AbsAndroidViewModel(application) {

    // 状态
    private val viewModelState = MutableStateFlow(CommunicationViewModelState())
    val uiState: StateFlow<CommunicationUiState> =
        viewModelState.map(CommunicationViewModelState::toUiState).stateIn(
            viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
        )
    //
    /** message 发送数据 */
    private var serviceMessenger: Messenger? = null

    // 连接 messengerService
    private val messengerServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            serviceMessenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceMessenger = Messenger(service)
        }
    }

    /** 接收服务端回执 */
    private var messengerThread = HandlerThread("messenger_thread").apply { start() }
    private val clientMessenger = Messenger(object : Handler(messengerThread.looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            addChatMessage(
                ChatMessage.Text(
                    author = "messenger",
                    content = "${msg.data.getString("replay")}",
                )
            )
        }
    })

    /** aidl 接口 */
    private var remoteService: IRemoteService? = null
    private val remoteServiceDeathRecipient = IBinder.DeathRecipient {
        ZLog.i("CommunicationViewModel", "remoteService binderDied")
        remoteService = null
        // 重连？
    }

    // 连接 remoteService
    private val remoteServiceServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            ZLog.i("CommunicationViewModel", "onRemoteServiceDisconnected")
            remoteService?.asBinder()?.unlinkToDeath(remoteServiceDeathRecipient, 0)
            remoteService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            remoteService = IRemoteService.Stub.asInterface(service)
            service?.linkToDeath(remoteServiceDeathRecipient, 0)
        }
    }

    private val replayReceiver = object : BroadcastReceiver() {
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
                addChatMessage(
                    ChatMessage.Text(
                        author = "BROADCAST",
                        content = ipcMessage?.data ?: "",
                    )
                )
            }
        }
    }

    private val webSocketManager = WebSocketManager()

    init {
        viewModelScope.launch {
            val url =
                "https://upload-bbs.miyoushe.com/upload/2023/04/02/75276539/4885786e111be28b08ad9e6193c3d02c_790687885649586205.png?x-oss-process=image//resize,s_600/quality,q_80/auto-orient,0/interlace,1/format,png"
            addChatMessage(
                ChatMessage.Image(
                    author = "me",
                    imageUrl = url,
                )
            )
            addChatMessage(
                ChatMessage.Image(
                    author = "me",
                    localPath = "/sdcard/Pictures/Screenshots/Screenshot_20220608-011943.png",
                )
            )
            addChatMessage(
                ChatMessage.Image(
                    author = "me",
                    localPath = "/sdcard/Android/data/com.zaze.demo/cache/image_1680696108783.jpg",
                )
            )
        }
    }

    fun sendMessage(message: String) {
        val chatMessage = ChatMessage.Text(
            author = "me",
            content = message,
        )
        actualSend(chatMessage)
    }

    private fun actualSend(message: ChatMessage) {
        addChatMessage(message)
        val messageContent = message.getMessageContent()
        when (viewModelState.value.communicationMode) {
            CommunicationMode.AIDL -> {
                addChatMessage(
                    ChatMessage.Text(
                        author = "aidl",
                        content = "${remoteService?.messageService?.message?.data}",
                    )
                )
//                if (message is ChatMessage.Image && !message.localPath.isNullOrEmpty()) {
//                    val uri = Uri.parse(message.localPath)
//                    ZLog.i(ZTag.TAG_DEBUG, "uri: $uri")
//                    viewModelScope.launch(Dispatchers.IO) {
//                        ZLog.i(ZTag.TAG_DEBUG, "name: ${uri.path?.split("/")?.last()}")
//                        val fd = FileProviderHelper.openFileDescriptor(application, uri, "r")
//                        remoteService?.writeFile(fd, "aaa.jpg")
//                        fd?.close()
//                        // 测试读取文件
//                        remoteService?.read("aaa.jpg")?.let {
//                            FileUtil.write(FileInputStream(it.fileDescriptor), FileOutputStream(File(application.externalCacheDir, "aaa.jpg")))
//                        }
//                    }
//                }
            }

            CommunicationMode.MESSENGER -> {
                val msg = Message.obtain()
                msg.replyTo = clientMessenger
                val bundle = Bundle()
                bundle.putString("content", messageContent)
                msg.data = bundle
                serviceMessenger?.send(msg)
            }

            CommunicationMode.BROADCAST -> {
                application.sendBroadcast(Intent(MessageReceiver.ACTION_MESSAGE).also {
                    it.putExtra(MessageReceiver.KEY_MESSAGE, IpcMessage(data = messageContent))
                })
            }

            CommunicationMode.SERVER -> {
                application.sendBroadcast(Intent(MessageReceiver.ACTION_MESSAGE).also {
                    it.putExtra(MessageReceiver.KEY_MESSAGE, IpcMessage(data = messageContent))
                })
            }

            CommunicationMode.SOCKET -> {
                webSocketManager.send(messageContent)
            }

            else -> {
                addChatMessage(
                    ChatMessage.Text(
                        author = "error",
                        content = "暂未实现该功能",
                    )
                )
            }
        }
    }

    fun sendPicture(filePath: String) {
        val chatMessage = ChatMessage.Image(
            author = "me",
            localPath = filePath,
        )
        actualSend(chatMessage)
    }

    fun onFileSend(files: List<String>) {
        viewModelScope.launch {
            files.forEach {
                val uri = Uri.parse(it)
                println("uri: $uri")
                if (DocumentsContract.isDocumentUri(application, uri)) {
                    // document类型，通过documentId 查询
                    val docId = DocumentsContract.getDocumentId(uri)
                }
                println("getType: ${application.contentResolver.getType(uri)}")
                uri.query(context = application) { cursor ->
                    while (cursor.moveToNext()) {
                        cursor.columnNames.forEach { name ->
                            val index = cursor.getColumnIndex(name)
                            val value: Any? = if (index >= 0) {
                                when (cursor.getType(index)) {
                                    Cursor.FIELD_TYPE_NULL -> {
                                        null
                                    }

                                    Cursor.FIELD_TYPE_INTEGER -> {
                                        cursor.getInt(index)
                                    }

                                    Cursor.FIELD_TYPE_FLOAT -> {
                                        cursor.getFloat(index)

                                    }

                                    Cursor.FIELD_TYPE_STRING -> {
                                        cursor.getString(index)
                                    }

                                    Cursor.FIELD_TYPE_BLOB -> {
                                        cursor.getBlob(index)
                                    }

                                    else -> {
                                        null
                                    }
                                }
                            } else {
                                null
                            }
                            println("columnNames: $name = $value")
                        }
                    }
                }

                val chatMessage = ChatMessage.File(
                    author = "me",
                    localPath = it,
                    mimeType = application.contentResolver.getType(uri)
                )
                actualSend(chatMessage)
            }
        }

    }

    fun onSwitchMode(communicationMode: CommunicationMode) {
        ZLog.i(ZTag.TAG + "IpcViewModel", "onSwitchMode: $communicationMode")
        viewModelState.update {
            it.copy(communicationMode = communicationMode)
        }
    }

    fun addChatMessage(message: ChatMessage) {
        ZLog.i(
            ZTag.TAG + "IpcViewModel",
            "addChatMessage ${viewModelState.value.messages.size}: $message"
        )
        viewModelState.update {
            val newList = it.messages.toMutableList()
            newList.add(0, message)
            it.copy(messages = newList)
        }
    }

    //
    private fun test() {
        val write = Parcel.obtain().apply {
            writeInt(2233)
        }
        val reply = Parcel.obtain()
        try {
            val serviceManager = Class.forName("android.os.ServiceManager");
            val method = serviceManager.getMethod("getService", String::class.java)
            val testBinder = method.invoke(null, "testBinder") as Binder
            ZLog.v(ZTag.TAG_DEBUG, "get testBinder success :$testBinder");
            testBinder.transact(0, write, reply, 0)
            val replayMessage = reply.readInt()
            ZLog.v(ZTag.TAG_DEBUG, "receiver testBinder message :$replayMessage");
        } catch (e: Exception) {
            ZLog.e(ZTag.TAG_DEBUG, "get testBinder fail");
        }
    }

    fun bind(context: Context) {
        ZLog.i("IpcScreen", "bindService")
        context.bindService(
            Intent(context, MessengerService::class.java),
            messengerServiceConnection,
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
            remoteServiceServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        context.registerReceiver(replayReceiver, IntentFilter(MessageReceiver.ACTION_REPLAY))
        webSocketManager.connect()
    }

    fun unBind(context: Context) {
        context.unbindService(messengerServiceConnection)
        context.unbindService(remoteServiceServiceConnection)
        context.unregisterReceiver(replayReceiver)
        webSocketManager.close()
    }


}

private data class CommunicationViewModelState(
    val communicationMode: CommunicationMode = CommunicationMode.AIDL,
    val messages: List<ChatMessage> = emptyList(),
    val me: String = "me",
) {
    fun toUiState(): CommunicationUiState {
        return CommunicationUiState(
            communicationMode = communicationMode,
            messages = messages,
            me = me
        )
    }
}

data class CommunicationUiState(
    val communicationMode: CommunicationMode,
    val messages: List<ChatMessage>,
    val me: String
)