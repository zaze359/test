package com.zaze.dynamic.android

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.annotation.RequiresApi
import com.zaze.dynamic.DynamicApk


class DynamicContext : ContextThemeWrapper {

    constructor() : super()
    constructor(base: Context?, themeResId: Int) : super(base, themeResId)

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(base: Context?, theme: Resources.Theme?) : super(base, theme)

    var plugin: DynamicApk? = null

    override fun getPackageName(): String {
        return plugin?.packageInfo?.packageName ?: super.getPackageName()
    }

    override fun getPackageManager(): PackageManager {
        return super.getPackageManager()
    }
    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    override fun getApplicationInfo(): ApplicationInfo {
        return plugin?.applicationInfo ?: super.getApplicationInfo()
    }

    override fun getResources(): Resources {
        return plugin?.resources ?: super.getResources()
    }

    override fun getAssets(): AssetManager {
        return plugin?.resources?.assets ?: super.getAssets()
    }

    override fun getSystemService(name: String): Any {
        return super.getSystemService(name)
    }

    override fun getClassLoader(): ClassLoader {
        return plugin?.classLoader ?: super.getClassLoader()
    }

    override fun startActivity(intent: Intent) {
        startActivity(intent, null)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        val pluginIntent = Intent(intent)
        pluginIntent.setExtrasClassLoader(classLoader)
        super.startActivity(pluginIntent, options)
    }
}