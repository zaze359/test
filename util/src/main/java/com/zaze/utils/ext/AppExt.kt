package com.zaze.utils.ext

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-27 - 09:24
 */
val ApplicationInfo.isSystemApp: Boolean
    get() = this.flags and ApplicationInfo.FLAG_SYSTEM > 0
val ApplicationInfo.debuggable: Boolean
    get() = this.flags and ApplicationInfo.FLAG_DEBUGGABLE > 0

fun ApplicationInfo.getLabel(context: Context): String =
    context.packageManager.getApplicationLabel(this).toString()

/**
 * 是否是 Android App Bundle 安装的应用
 */
val ApplicationInfo.isAAB: Boolean
    @RequiresApi(Build.VERSION_CODES.O)
    get() = !this.splitNames.isNullOrEmpty()

val PackageInfo.isAAB: Boolean get() = !this.splitNames.isNullOrEmpty()