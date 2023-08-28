package com.zaze.demo.feature.settings

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import com.google.android.material.color.DynamicColors
import com.zaze.core.designsystem.util.ThemeStore

class ThemeSettingsFragment : AbsSettingFragment() {
    override fun initPreferences() {
        findPreference<ListPreference?>(ThemeStore.Key.THEME_MODE)?.let {
            this.setSummary(it)
            it.setOnPreferenceChangeListener { preference, newValue ->
                updateSummary(preference, newValue)
                requireActivity().recreate()
                true
            }
        }
        findPreference<CheckBoxPreference?>(ThemeStore.Key.MATERIAL_YOU)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                DynamicColors.applyToActivitiesIfAvailable(requireActivity().application)
            }
            requireActivity().recreate()
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_pref_theme)
    }
}