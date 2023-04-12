package com.zaze.demo.feature.communication

import android.app.Application
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.core.model.data.ChatMessage
import com.zaze.core.model.data.getMessageContent
import com.zaze.demo.feature.communication.broadcast.MessageReceiver
import com.zaze.demo.feature.communication.parcel.IpcMessage
import com.zaze.utils.getImagePath
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(application: Application) :
    AbsAndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(CommunicationViewModelState())
    val uiState: StateFlow<CommunicationUiState> =
        viewModelState.map(CommunicationViewModelState::toUiState).stateIn(
            viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
        )

    /** 发送数据 */
    var serviceMessenger: Messenger? = null

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
    var remoteService: IRemoteService? = null

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
            content = "send: $message",
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
                        content = "${remoteService?.message?.message}",
                    )
                )
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
                    it.putExtra(MessageReceiver.KEY_MESSAGE, IpcMessage(message = messageContent))
                })
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