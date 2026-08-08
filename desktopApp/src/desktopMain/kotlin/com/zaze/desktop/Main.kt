package com.zaze.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.zaze.logic.core.util.formatBytes
import com.zaze.logic.core.util.isNotNullOrBlank

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "test-desktop") {
        App()
    }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("CMP + KMP 调试入口 (desktopApp)")
                Text("formatBytes(1536) = ${formatBytes(1536)}")
                Text("\"hello\".isNotNullOrBlank() = ${"hello".isNotNullOrBlank()}")
            }
        }
    }
}
