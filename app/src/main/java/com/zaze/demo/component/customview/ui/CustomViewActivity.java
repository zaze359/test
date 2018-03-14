package com.zaze.demo.component.customview.ui;


import android.os.Bundle;

import com.zaze.common.base.BaseActivity;
import com.zaze.common.widget.AddImageLayout;
import com.zaze.demo.R;
import com.zaze.demo.component.customview.presenter.CustomViewPresenter;
import com.zaze.demo.component.customview.presenter.impl.CustomViewPresenterImpl;
import com.zaze.demo.component.customview.view.CustomViewView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-21 10:38 1.0
 */
public class CustomViewActivity extends BaseActivity implements CustomViewView {
    @Bind(R.id.custom_add_image_layout)
    AddImageLayout customAddImageLayout;
    private CustomViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_activity);
        ButterKnife.bind(this);
        presenter = new CustomViewPresenterImpl(this);
        customAddImageLayout.setOnImageAddListener(new AddImageLayout.OnImageAddListener() {
            @Override
            public void onImageAddListener() {
                customAddImageLayout.addImageRes(R.mipmap.ic_folder_open_black_48dp);
            }
        });
    }

}