package com.zaze.demo.feature.communication.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zaze.core.model.data.ChatMessage
import com.zaze.demo.feature.communication.R
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.net.URLDecoder

@Composable
fun ConversationMessages(
    me: String,
    messages: List<ChatMessage>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true,
            state = scrollState,
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 90.dp))
                .asPaddingValues(),
        ) {
            items(messages) {
                Message(me, it, {})
            }
        }
    }
}

@Composable
private fun Message(
    me: String,
    chatMessage: ChatMessage,
    onAvatarClick: (Long) -> Unit
) {
    val isUserMe = me == chatMessage.author
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        if (isUserMe) {
            AuthorAndMessage(
                modifier = Modifier
                    .padding(start = 58.dp)
                    .weight(1f),
                msg = chatMessage,
                isUserMe = true,
                authorClicked = {

                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Avatar(modifier = Modifier.size(42.dp), chatMessage, true, onAvatarClick)
        } else {
            Avatar(modifier = Modifier.size(42.dp), chatMessage, false, onAvatarClick)
            Spacer(modifier = Modifier.width(16.dp))
            AuthorAndMessage(
                modifier = Modifier
                    .padding(end = 58.dp)
                    .weight(1f),
                msg = chatMessage,
                isUserMe = false,
                authorClicked = {

                }
            )
        }
    }
}

@Composable
private fun Avatar(
    modifier: Modifier,
    message: ChatMessage,
    isUserMe: Boolean,
    onAvatarClick: (Long) -> Unit
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }
    val painter = message.authorImage?.let { url ->
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            onState = {
                ZLog.d(ZTag.TAG, "Avatar onState ${it}: $message")
            }
        )
    } ?: painterResource(id = R.drawable.baseline_square)
    Box {
        Image(
            modifier = modifier
                .border(1.5.dp, borderColor, CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                .clip(CircleShape)
                .clickable {
                    onAvatarClick(message.userId)
                },
//            colorFilter = ColorFilter.tint(Color.Green, blendMode = BlendMode.Darken),
            painter = painter,
            contentScale = ContentScale.Fit,
            contentDescription = message.author
        )
        Text(modifier = Modifier.align(Alignment.Center), text = "${message.author[0]}")
    }
}

@Composable
fun AuthorAndMessage(
    msg: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (isUserMe) Alignment.End else Alignment.Start
    ) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.paddingFrom(LastBaseline, after = 8.dp)
        )
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val ChatBubbleShapeWithMe = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)


@Composable
fun ChatItemBubble(
    message: ChatMessage,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit // 处理 @
) {
    val backgroundBubbleColor: Color
    val shape: Shape
    if (isUserMe) {
        backgroundBubbleColor = MaterialTheme.colorScheme.primary
        shape = ChatBubbleShapeWithMe
    } else {
        backgroundBubbleColor = MaterialTheme.colorScheme.surfaceVariant
        shape = ChatBubbleShape
    }

    val modifier = Modifier.padding(16.dp)

    Surface(
        modifier = Modifier,
        color = backgroundBubbleColor,
        shape = shape
    ) {
        when (message) {
            is ChatMessage.Text -> {
                Text(
                    modifier = modifier,
                    text = "${message.author}: ${message.content}",
                    style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
                )
            }
            is ChatMessage.Image -> {
                ImageItem(
                    modifier = modifier,
                    url = message.localPath ?: message.imageUrl,
                )
            }
            is ChatMessage.File -> {
                FileItem(
                    modifier = modifier,
                    url = message.localPath ?: message.url,
                    mimeType = message.mimeType,
                )
            }

        }
    }
}

@Composable
private fun FileItem(
    modifier: Modifier,
    url: String?,
    mimeType: String?
) {
    if(mimeType?.startsWith("image/") == true) {
        ImageItem(
            modifier = modifier,
            url = url,
        )
    } else {
        Text(
            modifier = modifier,
            text = "${mimeType}: ${URLDecoder.decode(url)}",
            style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        )
    }

}

@Composable
private fun ImageItem(
    modifier: Modifier,
    url: String?,
) {
    url?.let {
//        Image(
//            painter = painterResource(R.drawable.place_holder),
////            painter = rememberAsyncImagePainter(
////                model = ImageRequest.Builder(LocalContext.current)
////                    .data(url)
////                    .size(1920, 1080)
////                    .crossfade(true)
////                    .build(),
////                onState = {
////                    ZLog.d(ZTag.TAG, "onState: $it")
////                }
////            ),
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.size(160.dp),
//            contentDescription = ""
//        )

        SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
//                .size(1920, 1080)
                .size(480, 270)
                .crossfade(true)
                .build(),
            loading = {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            },
            contentScale = ContentScale.Crop,
            error = {
                painterResource(id = R.drawable.place_holder)
            },
            contentDescription = stringResource(id = R.string.attached_image),
            onSuccess = {
                ZLog.d(ZTag.TAG_DEBUG, "AsyncImage onSuccess: $url")
            },
            onError = {
                ZLog.w(ZTag.TAG_DEBUG, "AsyncImage onError: $url")
            },
            onLoading = {
                ZLog.d(ZTag.TAG_DEBUG, "AsyncImage onLoading: $url")
            }
        )

        // ------------------

//        AsyncImage(
//            placeholder = painterResource(R.drawable.place_holder),
//            modifier = modifier,
//            contentScale = ContentScale.Crop,
//            error = painterResource(id = R.drawable.place_holder),
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(url)
//                .crossfade(true)
//                .build(),
//            contentDescription = stringResource(id = R.string.attached_image),
//            onSuccess = {
//                ZLog.d(ZTag.TAG_DEBUG, "AsyncImage onSuccess")
//            },
//            onError = {
//                ZLog.d(ZTag.TAG_DEBUG, "AsyncImage onError")
//            },
//            onLoading = {
//                ZLog.d(ZTag.TAG_DEBUG, "AsyncImage onLoading")
//            }
//        )
    }
}