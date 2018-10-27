package com.zaze.demo.component.progress;

import android.os.Bundle;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;

import androidx.annotation.Nullable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 02:47
 */
public class ProgressActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_fragment);
    }
}
