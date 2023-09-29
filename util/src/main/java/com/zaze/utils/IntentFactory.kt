package com.zaze.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

object IntentFactory {

    /**
     * @return 用于打开 系统设置 的Intent
     */
    fun settingsLauncherIntent(context: Context): Intent? {
        return context.packageManager.getLaunchIntentForPackage("com.android.settings")?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        }
    }

    /**
     * 系统设置-查看应用详情
     */
    fun applicationDetailsSettings(packageName: String): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
    }

    /**
     * 系统设置-使用情况访问权限
     */
    fun usageAccessSettings(): Intent {
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    }

    /**
     * 系统设置-文件管理权限
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun manageAllFilesAccessPermission(): Intent {
        return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
    }

    /**
     * 开启辅助功能配置页
     */
    fun accessibilitySettings(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    }

    /**
     * 开启显示在其他应用之上 配置页
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun manageOverlayPermission(): Intent {
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    }

    // ------------------------------------------
    // ------------------------------------------

    /**
     * 发送信息-邮件、短信等
     */
    fun sendTextMessage(message: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
    }

    /**
     * 拨号
     */
    fun dial(tel: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:${tel}")
        }
    }

    /**
     * 浏览器-打开指定网页
     */
    fun openWebUrl(url: String): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    }

    // --------------------------------------------------
//    private fun buildServicePendingIntent(context: Context, intent: Intent): PendingIntent {
//        return PendingIntent.getService(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }
}