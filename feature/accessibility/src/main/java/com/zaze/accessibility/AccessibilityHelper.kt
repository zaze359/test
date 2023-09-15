package com.zaze.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import com.zaze.utils.FileUtil
import com.zaze.utils.JsonUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.ext.jsonToList
import com.zaze.utils.ext.toJsonString
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-08 01:29
 */
object AccessibilityHelper {

    fun isAccessibilityEnabled(context: Context): Boolean {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return accessibilityManager.isEnabled
    }


//    private fun clickByCmd(node: AccessibilityNodeInfo) {
//        val cmd = "input tap ${(rect.left + rect.right) / 2} ${(rect.top + rect.bottom) / 2}"
//        ZCommand.execCmdForRes(cmd)
//    }

    fun searchNode(node: AccessibilityNodeInfo, key: String?): AccessibilityNodeInfo? {
        return searchNodeByText(node, key) ?: searchNodeByViewId(node, key)
    }

    fun searchNodeByText(node: AccessibilityNodeInfo, text: String?): AccessibilityNodeInfo? {
        if (text.isNullOrEmpty()) return null
        return node.findAccessibilityNodeInfosByText(text)
            .takeUnless { it.isNullOrEmpty() }?.get(0)
    }

    fun searchNodeByViewId(node: AccessibilityNodeInfo, id: String?): AccessibilityNodeInfo? {
        if (id.isNullOrEmpty() || node.packageName.isNullOrEmpty()) return null
        return node.findAccessibilityNodeInfosByViewId("${node.packageName}:id/$id")
            .takeUnless { it.isNullOrEmpty() }?.get(0)
    }

}

fun AccessibilityService.clickNode(node: AccessibilityNodeInfo?) {
    node ?: return
    val success = node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    // 若失败，则使用 GestureDescription 执行点击
    if (!success && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val rect = Rect()
        node.getBoundsInScreen(rect)
        val x = (rect.left + rect.right) / 2
        val y = (rect.top + rect.bottom) / 2

        val build = GestureDescription.Builder()
        val path = Path().apply {
            moveTo(x.toFloat(), y.toFloat())
        }
        build.addStroke(GestureDescription.StrokeDescription(path, 0, 50))
        dispatchGesture(build.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                ZLog.i(ZTag.TAG, "dispatchGesture onCompleted")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                ZLog.i(ZTag.TAG, "dispatchGesture onCancelled")
            }
        }, null)
    }
}