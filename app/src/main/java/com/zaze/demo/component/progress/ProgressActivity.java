package com.zaze.demo.component.progress;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 02:47
 */
public class ProgressActivity extends ZBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progress);
    }
}
