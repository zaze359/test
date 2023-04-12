package com.zaze.core.designsystem.components.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
    private val _messages: MutableStateFlow<List<SnackbarMessage>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<SnackbarMessage>> get() = _messages.asStateFlow()

    fun showMessage(message: SnackbarMessage) {
        _messages.update { currentMessages ->
            currentMessages + message
        }
    }

    fun showMessage(@StringRes messageTextId: Int) {
        showMessage(
            SnackbarMessage.WithRes(
                id = UUID.randomUUID().mostSignificantBits, messageTextId = messageTextId
            )
        )
    }

    fun showMessage(messageText: String) {
        showMessage(
            SnackbarMessage.WithString(
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

sealed interface SnackbarMessage {
    val id: Long

    data class WithString(
        override val id: Long = UUID.randomUUID().mostSignificantBits, val messageText: String
    ) : SnackbarMessage

    data class WithRes(
        override val id: Long = UUID.randomUUID().mostSignificantBits,
        @StringRes val messageTextId: Int
    ) : SnackbarMessage

}


fun SnackbarMessage.toTextString(resources: Resources): String {
    return when (this) {
        is SnackbarMessage.WithString -> {
            this.messageText
        }
        is SnackbarMessage.WithRes -> {
            resources.getString(messageTextId)
        }
    }
}

@Composable
fun SnackbarMessage.toTextString(): String {
    return toTextString(resources = resources())
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
