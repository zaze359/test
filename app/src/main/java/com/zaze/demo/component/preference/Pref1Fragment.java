package com.zaze.demo.component.preference;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.demo.R;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import androidx.annotation.Nullable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-05-18 - 15:55
 */
public class Pref1Fragment extends PreferenceFragment {

    CheckBoxPreference soundCBP;
    CheckBoxPreference vibrationCBP;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        addPreferencesFromResource(R.xml.main_pref);
        initPreferences();
        return view;
    }

    private void initPreferences() {
        soundCBP = (CheckBoxPreference) findPreference("key_sound");
        vibrationCBP = (CheckBoxPreference) findPreference("key_vibration");


        soundCBP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ZLog.i(ZTag.TAG_DEBUG, "soundCBP : " + newValue);
                return true;
            }
        });

        vibrationCBP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ZLog.i(ZTag.TAG_DEBUG, "vibrationCBP : " + newValue);
                return true;
            }
        });
    }
}
