package com.zaze.demo.compose.ui.components.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

/**
 * Description :
 * @author : zaze
 * @version : 2023-01-13 18:47
 */
object SnackbarManager {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(message: Message) {
        _messages.update { currentMessages ->
            currentMessages + message
        }
    }

    fun showMessage(@StringRes messageTextId: Int) {
        showMessage(
            Message.WithRes(
                id = UUID.randomUUID().mostSignificantBits, messageTextId = messageTextId
            )
        )
    }

    fun showMessage(messageText: String) {
        showMessage(
            Message.WithString(
                id = UUID.randomUUID().mostSignificantBits, messageText = messageText
            )
        )
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}

sealed interface Message {
    val id: Long

    data class WithString(
        override val id: Long = UUID.randomUUID().mostSignificantBits, val messageText: String
    ) : Message

    data class WithRes(
        override val id: Long = UUID.randomUUID().mostSignificantBits,
        @StringRes val messageTextId: Int
    ) : Message

}


fun Message.toTextString(resources: Resources): String {
    return when (this) {
        is Message.WithString -> {
            this.messageText
        }
        is Message.WithRes -> {
            resources.getString(messageTextId)
        }
    }
}

@Composable
fun Message.toTextString(): String {
    return toTextString(resources = resources())
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
