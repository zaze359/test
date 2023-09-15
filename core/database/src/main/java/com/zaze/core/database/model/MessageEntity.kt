package com.zaze.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zaze.core.model.data.ChatMessage

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey
    val id: Long,
    val author: String,
    val authorImage: String,
    val content: String,
    val timestamp: Long,
    val messageType: String
)


enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
}

fun MessageEntity.asExternalModel(): ChatMessage {
    return when (MessageType.valueOf(messageType)) {
        MessageType.TEXT -> {
            ChatMessage.Text(
                id = id,
                content = content,
                author = author,
                timestamp = timestamp,
                authorImage = authorImage
            )
        }

        MessageType.IMAGE -> {
            ChatMessage.Image(
                id = id,
                imageUrl = content,
                author = author,
                timestamp = timestamp,
                authorImage = authorImage
            )
        }

        MessageType.FILE -> {
            ChatMessage.File(
                id = id,
                localPath = content,
                author = author,
                timestamp = timestamp,
                authorImage = authorImage
            )
        }
    }
//    authorImage = if (author == "me") R.drawable.ic_person_pin_24 else R.drawable.ic_person_pin_24
}