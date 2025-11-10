package com.zaze.demo.component.webview.settings

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.zaze.common.base.BaseApplication
import com.zaze.demo.component.webview.FileChooserListener
import com.zaze.utils.log.ZLog

/**
 * Description:
 *
 * @author zhaozhen
 * @version 2025/8/26 16:25
 */
class CustomWebChromeClient(
    val fileChooserListener: FileChooserListener? = null,
) : WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?,
    ): Boolean {
        if (fileChooserParams != null) {
            if (fileChooserParams.isCaptureEnabled) {
                fileChooserListener?.showCamera(filePathCallback)
            } else { // 先默认都是拍照，需求就是选文件
                fileChooserListener?.showCamera(filePathCallback)
            }
            return true
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        if (request == null) {
            return
        }
        val requestedResources = request.resources
        val permissionsToRequest = mutableListOf<String>()
        if (requestedResources.contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if (requestedResources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (permissionsToRequest.isNotEmpty() && hasPermissions(permissionsToRequest)) {
            grant(request)
        } else {
            deny(request)
        }
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    private fun hasPermissions(permissions: List<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(BaseApplication.getInstance(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun grant(request: PermissionRequest?) {
        ZLog.i(
            "WebViewPermissions",
            "Granted permission to origin: ${request?.origin}; resource: ${request?.resources?.joinToString()}"
        )
        request?.grant(request.resources)
    }

    private fun deny(request: PermissionRequest?) {
        ZLog.w(
            "WebViewPermissions",
            "Denied permission to origin: ${request?.origin}; resource: ${request?.resources?.joinToString()}"
        )
        request?.deny()
    }

}