package com.zaze.demo.component.customview;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.zaze.common.base.AbsActivity;
import com.zaze.common.widget.AddImageLayout;
import com.zaze.demo.R;
import com.zaze.ui.skin.SkinLayoutInflaterFactory;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-21 10:38 1.0
 */
public class CustomViewActivity extends AbsActivity {
    AddImageLayout customAddImageLayout;
    CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        getLayoutInflater().setFactory2(new SkinLayoutInflaterFactory(getDelegate()::createView));
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_activity);

        customAddImageLayout = findViewById(R.id.custom_add_image_layout);
        customView = findViewById(R.id.custon_view);
        customAddImageLayout.setOnImageAddListener(new AddImageLayout.OnImageAddListener() {
            @Override
            public void onImageAddListener() {
//                customAddImageLayout.addImageRes(R.drawable.ic_folder_open_black_48dp);
                customView.customScrollTo(100, 100);
            }
        });

        // 通过 view.post() 获取view的宽高
        customView.post(() -> {
            printWH("view.post()");
        });

        // 通过 ViewTreeObserver 获取view的宽高
        ViewTreeObserver observer = customView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                customView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                printWH("ViewTreeObserver");
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 通过 onWindowFocusChanged 获取view的宽高
        printWH("onWindowFocusChanged()");
    }

    private void printWH(String caller) {
        ZLog.i(ZTag.TAG_DEBUG, caller + ": " + customView.getWidth() + ", " + customView.getHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("CustomViewActivity", "onTouchEvent: " + event.getAction());
//        return super.onTouchEvent(event);
//    }
}