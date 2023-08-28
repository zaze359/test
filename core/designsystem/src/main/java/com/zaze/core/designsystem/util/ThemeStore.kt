package com.zaze.core.designsystem.util

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager

class ThemeStore(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    object Key {
        // light、dark、auto
        const val THEME_MODE = "theme_mode"

        // 是否开启了 Material You
        // 由系统决定 强调色等色值
        const val MATERIAL_YOU = "material_you"
    }

    val themeMode: String?
        get() = preferences.getString(Key.THEME_MODE, null)
    val materialYou: Boolean
        get() = preferences.getBoolean(
            Key.MATERIAL_YOU,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        )

}