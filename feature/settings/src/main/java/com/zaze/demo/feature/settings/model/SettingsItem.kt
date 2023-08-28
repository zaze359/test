package com.zaze.demo.feature.settings.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class SettingsItem(
    @StringRes val titleRes: Int,
    @StringRes val summaryRes: Int,
    @DrawableRes val iconRes: Int,
    @ColorRes val iconBackgroundColor: Int,
    @IdRes val actionIdRes: Int
) {
    override fun toString(): String {
        return "SettingsItem(titleRes=$titleRes, summaryRes=$summaryRes, iconRes=$iconRes, iconBackgroundColor=$iconBackgroundColor, actionIdRes=$actionIdRes)"
    }
}
