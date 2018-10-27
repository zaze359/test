package com.zaze.demo.component.time;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.utils.date.ZDateUtil;

import java.util.TimeZone;

import androidx.annotation.Nullable;

/**
 * Description : 时间转换之类
 *
 * @author : ZAZE
 * @version : 2016-12-22 - 11:10
 */
public class TimeActivity extends BaseActivity {
    EditText timeInputEdt;
    TextView timeOutTv;
    CheckBox timeZoneCb;

    private boolean isGMT = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);
        timeInputEdt = findViewById(R.id.time_input_edt);
        timeOutTv = findViewById(R.id.time_out_tv);
        timeZoneCb = findViewById(R.id.time_zone_cb);
        timeZoneCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isGMT = isChecked;
            }
        });

        findViewById(R.id.time_execute_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute();
            }
        });
    }

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
