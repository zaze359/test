package com.zaze.demo.feature.intent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.zaze.core.designsystem.components.BackIconButton
import com.zaze.core.designsystem.components.Padding
import com.zaze.utils.AppUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

@Composable
internal fun IntentRoute(
    onBackPress: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    IntentScreen(onBackPress, snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IntentScreen(
    onBackPress: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "测试页面跳转")
                },
                navigationIcon = { BackIconButton(onBackPress = onBackPress) },
            )
        }
    ) {
        IntentList(Modifier.padding(it))
    }
}

@Composable
private fun IntentList(modifier: Modifier) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(start = 12.dp, end = 12.dp)
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            ZLog.d(ZTag.TAG_DEBUG, "StartActivityForResult: $result")
            // 多选
            result.data?.clipData?.let { clip ->
                repeat(clip.itemCount) {
                    ZLog.d(ZTag.TAG_DEBUG, "clipData: ${clip.getItemAt(it).uri}")
                }
            }
            // 单选
            result.data?.data?.let {
                ZLog.d(ZTag.TAG_DEBUG, "data: $it")
            }

        }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(context, EntranceActivity::class.java)
            intent.putExtra(EntranceActivity.KEY, "显示启动：EntranceActivity::class.java")
            context.startActivity(intent)
        }) {
            Text(text = "启动页面-显示Intent")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent("com.zaze.demo.action.ENTRANCE")
            intent.putExtra(
                EntranceActivity.KEY, "隐式启动\n" +
                        "action: com.zaze.demo.action.ENTRANCE\n" +
                        "category: android.intent.category.DEFAULT"
            )
            context.startActivity(intent)
        }) {
            Text(text = "启动页面-隐式Intent")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent("com.zaze.demo.action.ENTRANCE")
            intent.addCategory("com.zaze.demo.category.ENTRANCE")
            intent.putExtra(
                EntranceActivity.KEY, "隐式启动\n" +
                        "action: com.zaze.demo.action.ENTRANCE\n" +
                        "category: com.zaze.demo.category.ENTRANCE"
            )
            context.startActivity(intent)
        }) {
            Text(text = "启动页面-自定义category")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.example.com:80/entrance")
                setPackage(context.packageName)
                putExtra(
                    EntranceActivity.KEY, "Deeplink\n" +
                            "https://www.example.com:80/entrance"
                )
            }
            context.startActivity(intent)
        }) {
            Text(text = "启动页面-Deeplink")
        }
        // ----------------------------
        MyPadding()
        ElevatedButton(modifier = buttonModifier, onClick = {
            AppUtil.startApplication(context, "com.android.settings")
        }) {
            Text(text = "跳转系统设置")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }) {
            Text(text = "跳转系统设置-查看应用详情")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }) {
            Text(text = "跳转系统设置-使用情况访问权限")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ElevatedButton(modifier = buttonModifier, onClick = {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                context.startActivity(intent)
            }) {
                Text(text = "跳转系统设置-文件管理权限")
            }
        }
        // ----------------------------
        MyPadding()
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.baidu.com")
            context.startActivity(intent)
        }) {
            Text(text = "跳转浏览器-打开指定网页")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "测试发送")
                type = "text/plain"
            }
            context.startActivity(intent)
        }) {
            Text(text = "发送信息-邮件、短信等")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent().apply {
                action = Intent.ACTION_DIAL
                data = Uri.parse("tel:10086")
            }
            context.startActivity(intent)
        }) {
            Text(text = "拨号10086")
        }
        //
        ElevatedButton(modifier = buttonModifier, onClick = {
            val outputImage = File(context.externalCacheDir, "image_test.jpg")
            FileUtil.createFileNotExists(outputImage)
            val imageUri = FileProvider.getUriForFile(
                context,
                "com.zaze.demo.fileProvider",
                outputImage
            )
            // 打开相机
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            launcher.launch(intent)
        }) {
            Text(text = "打开相机")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            // 选择已存在的资源。音频、视频、通讯录、录音等
            // 只能单选，不支持多选
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
//            intent.type = "video/*"
//            intent.type = "*/*"
//            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            launcher.launch(intent)
        }) {
            Text(text = "选择图片-PICK")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
            // 支持用户运行时创建数据（拍照、录制等）。
            intent.type = "*/*"
            // 支持多选
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
            launcher.launch(Intent.createChooser(intent, "获取本地资源"))
        }) {
            Text(text = "获取本地资源-GET_CONTENT)")
        }
        ElevatedButton(modifier = buttonModifier, onClick = {
            // 选择一个或多个现有的文档
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            // 支持多选
            intent.type = "*/*"
            launcher.launch(intent)
        }) {
            Text(text = "获取本地文件-OPEN_DOCUMENT")
        }
    }
}

@Composable
private fun MyPadding() {
    Padding(0, 10, 10, 0)
}