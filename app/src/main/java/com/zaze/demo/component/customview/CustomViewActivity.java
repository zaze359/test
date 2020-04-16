package com.zaze.demo.component.customview;


import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.zaze.common.base.AbsActivity;
import com.zaze.common.widget.AddImageLayout;
import com.zaze.demo.R;

import org.jetbrains.annotations.Nullable;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-21 10:38 1.0
 */
public class CustomViewActivity extends AbsActivity {
    AddImageLayout customAddImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_activity);
        customAddImageLayout = findViewById(R.id.custom_add_image_layout);
        customAddImageLayout.setOnImageAddListener(new AddImageLayout.OnImageAddListener() {
            @Override
            public void onImageAddListener() {
                customAddImageLayout.addImageRes(R.mipmap.ic_folder_open_black_48dp);
            }
        });
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("CustomViewActivity", "onTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }
}