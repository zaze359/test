package com.zaze.demo.component.okhttp;


import android.os.Bundle;
import android.view.View;

import androidx.hilt.navigation.compose.HiltViewModelKt;
import androidx.lifecycle.ViewModelProvider;

import com.zaze.common.base.AbsActivity;
import com.zaze.demo.R;
import com.zaze.demo.debug.ApplicationManager;
import com.zaze.utils.JsonUtil;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-06-15 01:25 1.0
 */
@AndroidEntryPoint
public class OkHttpActivity extends AbsActivity {
//    @Inject
    public OkHttpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_http_activity);
        viewModel = new ViewModelProvider(this).get(OkHttpViewModel.class);
        findViewById(R.id.http_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.objToJson(ApplicationManager.INSTANCE.getAllApps());
//                viewModel.post();
                viewModel.get();
//                viewModel.add();
            }
        });
    }
}