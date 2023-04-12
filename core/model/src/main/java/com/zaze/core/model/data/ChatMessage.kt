package com.zaze.core.model.data

sealed class ChatMessage(
    open val id: Long,
    open val userId: Long,
    open val author: String,
    open val timestamp: Long,
    open val authorImage: String?
) {
    data class Text(
        override val id: Long = -1,
        override val userId: Long = -1L,
        override val author: String,
        override val timestamp: Long = System.currentTimeMillis(),
        override val authorImage: String? = null,
        val content: String,
    ) : ChatMessage(id, userId, author, timestamp, authorImage)

    data class Image(
        override val id: Long = -1,
        override val userId: Long = -1L,
        override val author: String,
        override val timestamp: Long = System.currentTimeMillis(),
        override val authorImage: String? = null,
        val localPath: String? = null,
        val imageUrl: String? = null,
    ) : ChatMessage(id, userId, author, timestamp, authorImage)

    data class File(
        override val id: Long = -1,
        override val userId: Long = -1L,
        override val author: String,
        override val timestamp: Long = System.currentTimeMillis(),
        override val authorImage: String? = null,
        val localPath: String? = null,
        val url: String? = null,
        val mimeType: String? = null
    ) : ChatMessage(id, userId, author, timestamp, authorImage)
}

fun ChatMessage.getMessageContent(): String {
    return when (this) {
        is ChatMessage.Text -> {
            content
        }
        is ChatMessage.Image -> {
            localPath ?: imageUrl ?: ""
        }
        is ChatMessage.File -> {
            localPath ?: url ?: ""
        }
    }
}