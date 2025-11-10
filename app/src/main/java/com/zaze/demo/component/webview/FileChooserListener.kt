package com.zaze.demo.component.webview

import android.net.Uri
import android.webkit.ValueCallback

/**
 * Description:
 *
 * @author zhaozhen
 * @version 2025/8/26 17:53
 */
interface FileChooserListener {
    fun showCamera(filePathCallback: ValueCallback<Array<Uri>>?)
}