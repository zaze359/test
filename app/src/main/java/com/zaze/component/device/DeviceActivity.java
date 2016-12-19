package com.zaze.component.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zaze.R;
import com.zaze.aarrepo.commons.base.BaseActivity;
import com.zaze.aarrepo.utils.LocalDisplay;
import com.zaze.aarrepo.utils.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-19 - 11:25
 */
public class DeviceActivity extends BaseActivity {

    @Bind(R.id.device_screen)
    TextView deviceScreen;
    @Bind(R.id.device_density)
    TextView deviceDensity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);
        deviceScreen.setText(format(
                "屏幕分辨率 : %dx%d",
                LocalDisplay.SCREEN_WIDTH_PIXELS,
                LocalDisplay.SCREEN_HEIGHT_PIXELS)
        );
        deviceDensity.setText(format("屏幕密度 : %1.2f", LocalDisplay.SCREEN_DENSITY));
    }


    private String format(String format, Object... args) {
        return StringUtil.format(format, args);
    }
}
