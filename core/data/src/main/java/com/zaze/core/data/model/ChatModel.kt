package com.zaze.core.data.model

import com.zaze.core.database.model.MessageEntity
import com.zaze.core.database.model.MessageType
import com.zaze.core.model.data.ChatMessage

fun ChatMessage.asEntity(): MessageEntity {
    return when (this) {
        is ChatMessage.Text -> {
            MessageEntity(
                id = id,
                author = author,
                authorImage = authorImage ?: "",
                content = content,
                timestamp = timestamp,
                messageType =  MessageType.TEXT.name
            )
        }
        is ChatMessage.Image -> {
            MessageEntity(
                id = id,
                author = author,
                authorImage = authorImage ?: "",
                content = imageUrl ?: "",
                timestamp = timestamp,
                messageType = MessageType.IMAGE.name
            )
        }

        is ChatMessage.File -> {
            MessageEntity(
                id = id,
                author = author,
                authorImage = authorImage ?: "",
                content = localPath ?: "",
                timestamp = timestamp,
                messageType =  MessageType.FILE.name
            )
        }
    }
}