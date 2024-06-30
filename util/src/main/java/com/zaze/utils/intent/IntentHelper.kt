package com.zaze.utils.intent

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo

/**
 * @Description:
 * @Author zhaozhen
 * @Date 2024/6/24 16:43
 */
class IntentHelper {
    /**
     * 是否是有效的Intent
     *
     * [context] context
     * [intent]  intent
     * @return true 有效的
     */
    fun isIntentEffective(context: Context, intent: Intent): Boolean {
        return query(context, intent).isNotEmpty()
    }

    fun query(context: Context, intent: Intent, flag: Int = 0): List<ResolveInfo> {
        return context.packageManager.queryIntentActivities(intent, flag)
    }

    fun getLaunchIntentForPackage(context: Context, packageName: String): Intent? {
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }
}