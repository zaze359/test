package com.zaze.demo.feature.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-05-18 - 14:27
 */
@Deprecated
public class MyPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Exit");
            setListFooter(button);
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
//        return super.isValidFragment(fragmentName);
        return true;
    }
}
