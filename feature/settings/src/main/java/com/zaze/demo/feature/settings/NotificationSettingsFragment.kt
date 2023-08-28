package com.zaze.demo.feature.settings

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class NotificationSettingsFragment : AbsSettingFragment() {
    override fun initPreferences() {
        findPreference<CheckBoxPreference>("key_sound")?.setOnPreferenceChangeListener { preference, newValue ->
            ZLog.i(ZTag.TAG_DEBUG, "key_sound: $newValue")
            true
        }

        findPreference<CheckBoxPreference>("key_vibration")?.setOnPreferenceChangeListener { preference, newValue ->
            ZLog.i(ZTag.TAG_DEBUG, "key_vibration: $newValue")
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_pref_notification)
    }
}