package com.zaze.demo.component.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.utils.date.ZDateUtil;

import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 时间转换之类
 *
 * @author : ZAZE
 * @version : 2016-12-22 - 11:10
 */
public class TimeActivity extends ZBaseActivity {
    @Bind(R.id.time_input_edt)
    EditText timeInputEdt;
    @Bind(R.id.time_out_tv)
    TextView timeOutTv;
    @Bind(R.id.time_zone_cb)
    CheckBox timeZoneCb;

    private boolean isGMT = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        ButterKnife.bind(this);
        timeZoneCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isGMT = isChecked;
            }
        });
    }


    @OnClick(R.id.time_execute_btn)
    public void execute() {
        String inputStr = timeInputEdt.getText().toString().trim();
        long timeMillis = Long.valueOf(inputStr);
        if (isGMT) {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        } else {
            TimeZone.setDefault(null);
        }

        timeOutTv.setText(ZDateUtil.timeMillisToString(timeMillis, "yyyy-MM-dd HH:mm:ss"));
    }


}
