package com.zaze.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-08 01:29
 */
object AccessibilityHelper {

    /**
     * 判断此应用的辅助功能是否已经开启了
     */
    fun isAccessibilityServiceOn(context: Context, serviceClass: Class<*>): Boolean {
        if (isAccessibilityEnabled(context)) {
            // 需要细分判断，获取启用的无障碍服务
            val services = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            ZLog.i(ZTag.TAG, "accessibilityEnabled SERVICES: $services")
            return services.split(":").find {
                it.equals("${context.packageName}/${serviceClass.canonicalName}", true)
            } != null
        }
        return false
    }

    /**
     * 判断无障碍功能是否可用
     * 只要有任意一个应用开启了 辅助功能，都会返回true。
     */
    private fun isAccessibilityEnabled(context: Context): Boolean {
//        var accessibilityEnabled = false
//        try {
//            accessibilityEnabled = Settings.Secure.getInt(
//                context.applicationContext.contentResolver,
//                Settings.Secure.ACCESSIBILITY_ENABLED
//            ) == 1
//        } catch (e: Settings.SettingNotFoundException) {
//            e.printStackTrace()
//        }

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
    if (!success) {
        val rect = Rect()
        node.getBoundsInScreen(rect)
        clickPoint((rect.left + rect.right) / 2F, (rect.top + rect.bottom) / 2F, 0, 50)
    }
}

fun AccessibilityService.clickPoint(x: Float, y: Float, startTime: Long, duration: Long) {
    // 若失败，则使用 GestureDescription 执行点击
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val build = GestureDescription.Builder()
        val path = Path().apply {
            moveTo(x, y)
        }
        build.addStroke(GestureDescription.StrokeDescription(path, startTime, duration))
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