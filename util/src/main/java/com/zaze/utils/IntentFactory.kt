package com.zaze.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
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

    /**
     * 开启辅助功能配置页
     */
    fun accessibilitySettings(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    }

    /**
     * 特殊权限
     */
    object SpecialPermission {
        /**
         * 使用情况访问权限
         */
        fun usageAccessSettings(): Intent {
            return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        }

        /**
         * 所有文件访问权限
         */
        @RequiresApi(Build.VERSION_CODES.R)
        fun manageAllFilesAccessPermission(): Intent {
            return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        }

        /**
         * 闹钟和提醒
         */
        @RequiresApi(Build.VERSION_CODES.S)
        fun requestScheduleExactAlarm(): Intent {
            return Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        }

        /**
         * 设备管理应用
         * [deviceAdmin]: 自定义的 ComponentName
         * [explanation]: 说明
         */
        fun addDeviceAdmin(deviceAdmin: ComponentName, explanation: String): Intent {
            return Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
                putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, explanation)
            }
        }

        /**
         * 显示在其他应用的上层
         * TYPE_APPLICATION_OVERLAY
         * 使用 Settings.canDrawOverlays() 判断 是否开启
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun manageOverlayPermission(): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }

        /**
         * 勿扰
         * android.permission.ACCESS_NOTIFICATION_POLICY
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun notificationPolicyAccessSettings(): Intent {
            return Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        }

        /**
         * 修改系统设置
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun manageWriteSettings(): Intent {
            return Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        }


        /**
         * 设备和应用通知
         * 允许监听通知
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
        fun notificationListenerSettings(): Intent {
            return Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        }

        /**
         * 媒体管理应用
         */
        @RequiresApi(Build.VERSION_CODES.S)
        fun requestManageMedia(): Intent {
            return Intent(Settings.ACTION_REQUEST_MANAGE_MEDIA)
        }

        /**
         * 安装未知应用
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun manageUnknownAppSources(): Intent {
            return Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        }

        /**
         * WLAN控制
         */
    }

    /**
     * 同步配置
     * 密码和账号
     */
    fun syncSettings(): Intent {
        return Intent(Settings.ACTION_SYNC_SETTINGS)
    }

    /**
     * 应用通知配置页
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun allAppsNotificationSettings(): Intent {
        return Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)
    }

    /**
     * 通知配置
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun notificationAssistantSettings(): Intent {
        return Intent(Settings.ACTION_NOTIFICATION_ASSISTANT_SETTINGS)
    }

    /**
     * WIFI 设置
     */
    fun wifiSettings(): Intent {
        return Intent(Settings.ACTION_WIFI_SETTINGS)
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