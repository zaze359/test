package com.zaze.demo.feature.communication.conversation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.zaze.demo.feature.communication.R
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


enum class InputSelector {
    NONE,
    MAP,
    DM,
    EMOJI,
    PHONE,
    FILE,
    CAMERA,
    PICTURE
}

enum class EmojiStickerSelector {
    EMOJI,
    STICKER
}


@Composable
fun IpcInput(
    modifier: Modifier = Modifier,
    onMessageSent: (String) -> Unit,
    onPictureSend: (String) -> Unit,
    onFileSend: (List<String>) -> Unit,
    snackbarHostState: SnackbarHostState,
    resetScroll: () -> Unit = {}
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    if (currentInputSelector != InputSelector.NONE) {
        // 若输入法打开， 返回时先关闭输入法
        BackHandler(onBack = dismissKeyboard)
    }
    // 保存输入框焦点状态
    var textFieldFocusState by remember { mutableStateOf(false) }
    // 保存输入的文本
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val context = LocalContext.current
    // 不允许的权限
    var notAvailablePermission by remember { mutableStateOf("") }

    var picUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val settingLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            Log.i("settingLauncher", "settingLauncher-----------------")
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                picUri?.toString()?.let(onPictureSend)
            }
        }

    val photoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                uri?.toString()?.let(onPictureSend)
            }
        }
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val list = ArrayList<String>()
                result.data?.clipData?.let { clip ->
                    repeat(clip.itemCount) {
                        list.add(clip.getItemAt(it).uri.toString())
                    }
                } ?: result.data?.data?.let {
                    list.add(it.toString())
                }
                onFileSend(list)
            }
        }

    val coroutineScope = rememberCoroutineScope()
    Surface(tonalElevation = 2.dp) {
        Column(modifier = modifier) {
            UserInputText(
                textFieldValue = textState,
                keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
                onTextFieldFocused = { focused ->
                    if (focused) {
                        currentInputSelector = InputSelector.NONE
                        resetScroll()
                    }
                    textFieldFocusState = focused
                },
                onTextChanged = { textState = it },
                focusState = textFieldFocusState
            )
            UserInputSelector(
                currentInputSelector = currentInputSelector,
                onSelectorChange = {
                    currentInputSelector = it
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("open $currentInputSelector")
                    }
                },
                sendMessageEnabled = textState.text.isNotEmpty(),
                onMessageSent = {
                    onMessageSent(textState.text)
                    // 清空输入内容
                    textState = TextFieldValue()
                    // 发送消息后，回到最下面
                    resetScroll()
                    // 关闭输入法
                    dismissKeyboard()
                },
                onPermissionNotAvailable = {
                    notAvailablePermission = it
                },
                openCamera = {
                    coroutineScope.launch {
                        val outputImage = File(
                            context.externalCacheDir,
                            "image_${System.currentTimeMillis()}.jpg"
                        )
                        FileUtil.createFileNotExists(outputImage)
                        val imageUri = FileProvider.getUriForFile(
                            context,
                            "com.zaze.demo.fileProvider",
                            outputImage
                        )
                        //
                        picUri = imageUri
                        // 打开相机
                        val intent = Intent("android.media.action.IMAGE_CAPTURE")
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        cameraLauncher.launch(intent)
                    }
                },
                openPhoto = {
                    // 打开相册
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    photoLauncher.launch(intent)
                },
                openFileManager = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    fileLauncher.launch(Intent.createChooser(intent, "获取本地资源"))
                }
            )
            SelectorExpanded(
                onCloseRequested = dismissKeyboard,
                onTextAdded = { textState = textState.addText(it) },
                currentSelector = currentInputSelector,
            )
        }

        LaunchedEffect(key1 = notAvailablePermission) {
            if (notAvailablePermission.isEmpty()) {
                return@LaunchedEffect
            }
            launch {
                when (notAvailablePermission) {
                    Manifest.permission.CAMERA -> {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = "访问相机权限被拒绝",
                            actionLabel = "设置",
                            withDismissAction = true
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                    Uri.fromParts("package", context.packageName, null)
                                )
                            settingLauncher.launch(intent)
                        }
                    }
                }
                notAvailablePermission = ""
            }
        }
    }
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

/**
 * 文本输入
 */
@Composable
fun UserInputText(
    keyboardShown: Boolean,
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    val a11ylabel = stringResource(id = R.string.textfield_desc)
    ZLog.d(ZTag.TAG, "focusState: $focusState")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = a11ylabel
                keyboardShownProperty = keyboardShown
            },
    ) {
        var lastFocusState by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .heightIn(min = 64.dp)
                .weight(1f)
                .align(Alignment.Bottom)
        ) {
            BasicTextField(
                modifier = createInputModifier()
                    .onFocusChanged { state ->
                        if (lastFocusState != state.isFocused) {
                            onTextFieldFocused(state.isFocused)
                        }
                        lastFocusState = state.isFocused
                    },
                value = textFieldValue,
                onValueChange = onTextChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                maxLines = 5,
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
            )

            // 未获取到焦点且内容为空时的提示文本。
            if (textFieldValue.text.isEmpty() && !focusState) {
                Text(
                    modifier = createInputModifier(),
                    text = stringResource(id = R.string.textfield_hint),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    }
}

private fun BoxScope.createInputModifier() = Modifier
    .fillMaxWidth()
    .padding(horizontal = 32.dp, vertical = 8.dp)
    .align(Alignment.CenterStart)

@Composable
fun UserInputSelector(
    currentInputSelector: InputSelector,
    onSelectorChange: (InputSelector) -> Unit,
    onMessageSent: () -> Unit,
    sendMessageEnabled: Boolean,
    openCamera: () -> Unit,
    openPhoto: () -> Unit,
    openFileManager: () -> Unit,
    onPermissionNotAvailable: suspend CoroutineScope.(String) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.i("UserInputSelector", "UserInputSelector-----------------")
    Row(
        modifier = modifier
            .height(72.dp)
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.EMOJI) },
            icon = Icons.Outlined.Mood,
            selected = false,
            description = stringResource(id = R.string.emoji_selector_bt_desc)
        )

        PermissionSelectorButton(
            permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            },
            onClick = {
                onSelectorChange(InputSelector.FILE)
                openFileManager()
            },
            icon = Icons.Outlined.AttachFile,
            selected = false,
            description = stringResource(id = R.string.attach_file_desc),
            onPermissionNotAvailable = onPermissionNotAvailable
        )
        PermissionSelectorButton(
            permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            },
            onClick = {
                onSelectorChange(InputSelector.PICTURE)
                openPhoto()
            },
            icon = Icons.Outlined.Image,
            selected = false,
            description = stringResource(id = R.string.attach_photo_desc),
            onPermissionNotAvailable = onPermissionNotAvailable

        )
        PermissionSelectorButton(
            onClick = {
                onSelectorChange(InputSelector.CAMERA)
                openCamera()
            },
            icon = Icons.Outlined.PhotoCamera,
            selected = false,
            description = stringResource(id = R.string.camera_desc),
            permission = Manifest.permission.CAMERA,
            onPermissionNotAvailable = onPermissionNotAvailable
        )
        Spacer(modifier = Modifier.weight(1f))
        SendButton(onMessageSent = onMessageSent, sendMessageEnabled = sendMessageEnabled)
    }
}


@Composable
fun SendButton(
    onMessageSent: () -> Unit,
    sendMessageEnabled: Boolean,
) {
    val border = if (!sendMessageEnabled) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        null
    }
    val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    val buttonColors = ButtonDefaults.buttonColors(
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor
    )
    Button(
        modifier = Modifier.height(36.dp),
        enabled = sendMessageEnabled,
        onClick = onMessageSent,
        colors = buttonColors,
        border = border,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            stringResource(id = R.string.send),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun SelectorExpanded(
    currentSelector: InputSelector,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    when (currentSelector) {
        InputSelector.NONE -> {
            return
        }
        InputSelector.CAMERA -> {

        }
        else -> {
        }
    }

//    val focusRequester = FocusRequester()
//    SideEffect {
//        if (currentSelector == InputSelector.EMOJI) {
//            focusRequester.requestFocus()
//        }
//    }

//    Surface(tonalElevation = 8.dp) {
//        when (currentSelector) {
//            InputSelector.EMOJI -> EmojiSelector(onTextAdded, focusRequester)
//            InputSelector.DM -> NotAvailablePopup(onCloseRequested)
//            InputSelector.PICTURE -> FunctionalityNotAvailablePanel()
//            InputSelector.MAP -> FunctionalityNotAvailablePanel()
//            InputSelector.PHONE -> FunctionalityNotAvailablePanel()
//            else -> { throw NotImplementedError() }
//        }
//    }
}


// ----------------

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}


@Preview
@Composable
fun UserInputPreview() {
    IpcInput(
        onMessageSent = {},
        snackbarHostState = remember {
            SnackbarHostState()
        },
        onPictureSend = {},
        onFileSend = {},
    )
}
